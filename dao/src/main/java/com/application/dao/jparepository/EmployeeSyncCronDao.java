package com.application.dao.jparepository;

import com.application.dao.entities.EmployeeSyncCron;
import com.application.dao.enums.SyncStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EmployeeSyncCronDao extends JpaRepository<EmployeeSyncCron, String> {



    @Query("select cron.employeeId from EmployeeSyncCron cron "
            + "inner join Employee emp "
            + " on emp.id = cron.employeeId "
            + " where emp.syncStatus in :statusList "
            + " and cron.nextTriggerTime <= :triggerTimeBefore ")
    List<String> fetchApplicableEmployeeIds(@Param("statusList")List<SyncStatus> statusList,
                                            @Param("triggerTimeBefore") LocalDateTime localDateTime, Pageable pageable);
}
