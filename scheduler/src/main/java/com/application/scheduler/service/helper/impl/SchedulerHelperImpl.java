package com.application.scheduler.service.helper.impl;

import com.application.commons.exceptions.BusinessException;
import com.application.commons.producers.MessageProducer;
import com.application.dao.annotations.ReadOnly;
import com.application.dao.entities.EmployeeSyncCron;
import com.application.dao.enums.SyncStatus;
import com.application.dao.jparepository.EmployeeSyncCronDao;
import com.application.scheduler.config.CronConfig;
import com.application.scheduler.cron.CronType;
import com.application.scheduler.service.helper.SchedulerHelper;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SchedulerHelperImpl implements SchedulerHelper {

    @Autowired
    @Qualifier("highThroughtputKafkaProducer")
    MessageProducer<String, String> kafkaPushService;

    @Autowired
    EmployeeSyncCronDao cronDao;

    @Autowired
    CronConfig cronConfig;

    @Override
    @ReadOnly
    public List<String> fetchObjectList(Pageable pageRequest, List<SyncStatus> syncStatusList) {
        return cronDao.fetchApplicableEmployeeIds(syncStatusList, LocalDateTime.now(), pageRequest);
    }


    @Override
    public void pushMessage(String topic, List<String> messages){
        kafkaPushService.sendMessage(topic, messages);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateStatusCronDetails(List<String> employeeIds, CronType cronType) {

        //pull out the statusCronDetails object
        List<EmployeeSyncCron> statusCronDetails = cronDao.findAllById(employeeIds);
        Preconditions.checkArgument(!CollectionUtils.isEmpty(statusCronDetails), " Could not fetch crons for orderList " + employeeIds);
        Preconditions.checkArgument(statusCronDetails.size() == employeeIds.size(), " Crons could not be fetched for some of the orders ");

        statusCronDetails.forEach(statusCronDetail -> {
            updateCronDetails(cronType, statusCronDetail);
        });

        cronDao.saveAll(statusCronDetails);
    }

    private void updateCronDetails(CronType cronType, EmployeeSyncCron statusCronDetailsInDb) {
        switch (cronType) {
            case CRON_TYPE_SYNC:
                statusCronDetailsInDb.setInitiatedCronCount(statusCronDetailsInDb.getInitiatedCronCount() + 1);
                updateNextTriggerTime(statusCronDetailsInDb.getInitiatedCronCount(), statusCronDetailsInDb, cronConfig.getSyncPayrollCronConfig());
                break;
            default:
                throw new BusinessException("Case not handled for " + cronType);
        }
    }

    private void updateNextTriggerTime(int totalRetriesTillDate,
                                       EmployeeSyncCron statusCronDetailsInDb,
                                       CronConfig.ChildCronConfig childCronConfig) {

        int retryCountThreshold = childCronConfig.getRetryCountThreshold();
        int x = totalRetriesTillDate / retryCountThreshold;    //TODO: need to rename x here
        int triggerTimeMultiplier = childCronConfig.getTriggerTimeMultiplier();
        long cronDelayInMin = Long.parseLong(childCronConfig.getFixedDelayInMs()) / 60000;
        LocalDateTime newNextTriggerTime = LocalDateTime.now().plusMinutes(x * (triggerTimeMultiplier * cronDelayInMin));
        statusCronDetailsInDb.setNextTriggerTime(newNextTriggerTime);
    }



}
