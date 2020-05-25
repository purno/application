package com.application.dao.jparepository;

import com.application.dao.entities.EmployeeSyncCron;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeSyncCronDao extends JpaRepository<EmployeeSyncCron, String> {
}
