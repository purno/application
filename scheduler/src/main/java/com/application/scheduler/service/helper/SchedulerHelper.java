package com.application.scheduler.service.helper;

import com.application.dao.enums.SyncStatus;
import com.application.scheduler.cron.CronType;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SchedulerHelper {
    List<String> fetchObjectList(Pageable pageRequest, List<SyncStatus> syncStatusList);

    void pushMessage(String topic, List<String> messages);

    @Transactional(rollbackFor = Exception.class)
    void updateStatusCronDetails(List<String> orderIds, CronType cronType);
}
