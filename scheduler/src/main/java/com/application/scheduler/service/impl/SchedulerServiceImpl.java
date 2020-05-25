package com.application.scheduler.service.impl;

import com.application.dao.enums.SyncStatus;
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

import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
@Slf4j
public class SchedulerServiceImpl implements SchedulerService {


    @Autowired
    SchedulerHelper schedulerHelper;

    @Autowired
    ExecutorServiceProvider executionService;


    @Override
    public void executeStatusUpdateCron(CronType cronType, CronConfig.ChildCronConfig childCronConfig, List<SyncStatus> syncStatusList, String kafkaTopic) throws InterruptedException {

        log.info("CRON TYPE >> " + cronType);

        //fetch a pool of threads (designated on cron type basis)
        ExecutorService executorService = executionService.getExecutor(cronType);
        boolean moreRecordsRemaining = true;
        int pageNum = 0, pageSize = childCronConfig.getBatchSize();

        while (moreRecordsRemaining) {
            log.info("fetching next set of records for page {} size {} ", pageNum, pageSize);
            Pageable pageRequest = PageRequest.of(pageNum, pageSize);
            List<String> ObjectList = schedulerHelper.fetchObjectList(pageRequest);
            if (ObjectList.size() == 0) {
                log.info("No more records..");
                moreRecordsRemaining = false;
                continue;
            }

            log.info("fetched : " + ObjectList);
            Runnable task = new SampleEmptyJob(ObjectList, schedulerHelper, kafkaTopic);
            executorService.submit(task);
            pageNum++;
            Thread.sleep(childCronConfig.getSleepTimeAfterEachIterationInMs());
        }
    }
}
