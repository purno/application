package com.application.service.impl;

import com.application.commons.constants.KafkaTopics;
import com.application.commons.producers.MessageProducer;
import com.application.dao.entities.Employee;
import com.application.dao.enums.SyncStatus;
import com.application.service.EmployeeService;
import com.application.service.dto.request.CreateEmployeeDto;
import com.application.service.helper.EmployeeHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    EmployeeHelper employeeHelper;

    @Autowired
    @Qualifier("defaultKafkaProducer")
    MessageProducer<String, String> kafkaPushService;


    @Override
    public void createEmployee(CreateEmployeeDto createEmployeeDto) throws IOException {
        // validate the input request
        createEmployeeDto.validate();
        // create employee in sync status pending
        Employee employee = employeeHelper.createAndSaveEmployee(createEmployeeDto);
        // trigger employee id for kafka sync
        kafkaPushService.sendMessage(KafkaTopics.EMPLOYEE_INIT, employee.getId());
    }


    @Override
    public List<Employee> fetchEmployees(String name, int age, List<String> empId, String alias, Pageable pageable){
        return employeeHelper.fetchEmployeeWithParams(name, SyncStatus.getListOfStatusForSyncSuccess(), age, empId, alias, pageable);
    }




}
