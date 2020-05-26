package com.application.service;

import com.application.dao.entities.Employee;
import com.application.service.dto.request.CreateEmployeeDto;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface EmployeeService {
    void createEmployee(CreateEmployeeDto createEmployeeDto) throws IOException;

    List<Employee> fetchEmployees(String name, int age, List<String> empId, String alias, Pageable pageable);
}
