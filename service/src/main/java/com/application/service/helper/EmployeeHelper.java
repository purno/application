package com.application.service.helper;

import com.application.dao.annotations.ReadOnly;
import com.application.dao.entities.Employee;
import com.application.dao.enums.SyncStatus;
import com.application.service.dto.request.CreateEmployeeDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface EmployeeHelper {
    @Transactional(rollbackFor = Exception.class)
    Employee createAndSaveEmployee(CreateEmployeeDto createEmployeeDto) throws JsonProcessingException;

    @ReadOnly
    List<Employee> fetchEmployeeWithParams(String name, List<SyncStatus> statusList, int age, List<String> empId, String alias, Pageable pageable);
}
