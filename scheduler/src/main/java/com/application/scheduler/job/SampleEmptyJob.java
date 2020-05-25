package com.application.scheduler.job;

import com.application.scheduler.cron.CronType;
import com.application.scheduler.service.helper.SchedulerHelper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@AllArgsConstructor
public class SampleEmptyJob implements Runnable {

    List<String> employeeIdList;
    SchedulerHelper schedulerHelper;
    String topic;

    @Override
    public void run() {
        try {
            schedulerHelper.pushMessage(topic, employeeIdList);
            schedulerHelper.updateStatusCronDetails(employeeIdList, CronType.CRON_TYPE_SYNC);
        } catch (Exception e) {
            log.error(" error received while invoking the api " + e.getMessage());
        }
    }
}
