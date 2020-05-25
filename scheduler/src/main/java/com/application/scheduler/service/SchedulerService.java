package com.application.scheduler.service;

import com.application.scheduler.config.CronConfig;
import com.application.scheduler.cron.CronType;

public interface SchedulerService {
    void executeStatusUpdateCron(CronType cronType, CronConfig.ChildCronConfig childCronConfig) throws InterruptedException;
}
