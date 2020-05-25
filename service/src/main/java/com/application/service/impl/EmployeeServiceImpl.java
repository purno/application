package com.application.service.impl;

import com.application.commons.constants.KafkaTopics;
import com.application.commons.producers.MessageProducer;
import com.application.dao.entities.Employee;
import com.application.service.EmployeeService;
import com.application.service.dto.request.CreateEmployeeDto;
import com.application.service.helper.EmployeeHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;

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
}
