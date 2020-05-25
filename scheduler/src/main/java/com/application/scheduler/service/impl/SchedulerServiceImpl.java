package com.application.scheduler.service.impl;

import com.application.scheduler.config.CronConfig;
import com.application.scheduler.cron.CronType;
import com.application.scheduler.job.SampleEmptyJob;
import com.application.scheduler.service.SchedulerService;
import com.application.scheduler.service.helper.SchedulerHelper;
import com.application.scheduler.utils.ExecutorServiceProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SchedulerServiceImpl implements SchedulerService {


    @Autowired
    SchedulerHelper schedulerHelper;

    @Autowired
    ExecutorServiceProvider executionService;


    @Override
    public void executeStatusUpdateCron(CronType cronType, CronConfig.ChildCronConfig childCronConfig) throws InterruptedException {

        log.info("CRON TYPE >> " + cronType);

        //fetch a pool of threads (designated on cron type basis)
        ExecutorService executorService = executionService.getExecutor(cronType);
        boolean moreRecordsRemaining = true;
        int pageNum = 0, pageSize = childCronConfig.getBatchSize();
        LocalDateTime timeNow = LocalDateTime.now();


        while (moreRecordsRemaining) {
            log.info("fetching next set of records for page {} size {} ", pageNum, pageSize);
            Pageable pageRequest = PageRequest.of(pageNum, pageSize);
            List<Object> ObjectList = schedulerHelper.fetchObjectList(pageRequest);
            if (ObjectList.size() == 0) {
                log.info("No more records..");
                moreRecordsRemaining = false;
                continue;
            }

            log.info("fetched : " + ObjectList);
            List<Callable<Object>> jobs = ObjectList.stream().map(address -> {
                Runnable task = new SampleEmptyJob();
                return Executors.callable(task);
            }).collect(Collectors.toList());
            executorService.invokeAll(jobs);
            pageNum++;
            Thread.sleep(childCronConfig.getSleepTimeAfterEachIterationInMs());
        }

    }
}
