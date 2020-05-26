package com.application.service.impl;

import com.application.service.dto.request.CreateEmployeeDto;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class EmployeeServiceTestImpl {

    CreateEmployeeDto employee = new CreateEmployeeDto();

    @Test(expected = IllegalArgumentException.class)
    public void createNewEmployeeWithAgeValidation() {
        employee.setName("Rajeev");
        employee.setAge(-25);
        employee.setSalary(new BigDecimal(5000));
        employee.validate();
    }


    @Test(expected = IllegalArgumentException.class)
    public void createNewEmployeeWithSalaryValidation() {
        employee.setName("Rajeev");
        employee.setAge(5);
        employee.setSalary(new BigDecimal(-1));
        employee.validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNewEmployeeWithNameValidation() {
        employee.setAge(10);
        employee.setSalary(new BigDecimal(10));
        employee.validate();
    }

    @Test
    public void createNewEmployee() throws IOException {
        employee.setName("TEST");
        employee.setAge(10);
        employee.setSalary(new BigDecimal(10));
        employee.validate();
    }


}
