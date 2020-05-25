package com.application.service;

import com.application.service.dto.request.CreateEmployeeDto;

import java.io.IOException;

public interface EmployeeService {
    void createEmployee(CreateEmployeeDto createEmployeeDto) throws IOException;
}
