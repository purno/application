package com.application.service;

import com.application.dao.entities.Employee;
import com.application.integration.exceptions.IntegrationException;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

public interface PayrollService {
    @Transactional(rollbackFor = Exception.class)
    Employee syncPayrollWithEmployeeData(String employeeId) throws IOException, IntegrationException;
}
