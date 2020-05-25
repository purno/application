package com.application.scheduler.cron;

import com.application.commons.constants.KafkaTopics;
import com.application.dao.enums.SyncStatus;
import com.application.scheduler.config.CronConfig;
import com.application.scheduler.service.SchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = {"classpath:resources/scheduler-cron-${spring.profiles.active}.properties"}, ignoreResourceNotFound = true)
@Slf4j
public class EmployeePayrollSyncCron {



    @Autowired
    CronConfig cronConfig;

    @Autowired
    SchedulerService schedulerService;

    @Scheduled(fixedDelayString = "${cron-config.payroll_sync_cron_config.fixed_delay_in_ms}")
    public void processLocationSync() throws InterruptedException {
        try {
            schedulerService.executeStatusUpdateCron(CronType.CRON_TYPE_SYNC,
                    cronConfig.getSyncPayrollCronConfig(), SyncStatus.getListOfStatusForSyncPending(),
                    KafkaTopics.EMPLOYEE_INIT);

        } catch (Exception e) {
            log.error("exception occured..", e.getMessage(), e);
        }
    }



}
