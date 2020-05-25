package com.application.scheduler.service;

import com.application.dao.enums.SyncStatus;
import com.application.scheduler.config.CronConfig;
import com.application.scheduler.cron.CronType;

import java.util.List;

public interface SchedulerService {
    void executeStatusUpdateCron(CronType cronType, CronConfig.ChildCronConfig childCronConfig, List<SyncStatus> syncStatusList, String kafkaTopic) throws InterruptedException;
}
