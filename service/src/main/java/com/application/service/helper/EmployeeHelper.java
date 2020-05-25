package com.application.service.helper;

import com.application.dao.entities.Employee;
import com.application.service.dto.request.CreateEmployeeDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.transaction.annotation.Transactional;

public interface EmployeeHelper {
    @Transactional(rollbackFor = Exception.class)
    Employee createAndSaveEmployee(CreateEmployeeDto createEmployeeDto) throws JsonProcessingException;
}
