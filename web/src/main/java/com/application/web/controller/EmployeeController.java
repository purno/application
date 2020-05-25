package com.application.web.controller;

import com.application.commons.utils.ResponseGenerator;
import com.application.service.EmployeeService;
import com.application.service.dto.request.CreateEmployeeDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Slf4j
@RequestMapping(value = "/api/v1/employee")
public class EmployeeController {


    @Autowired
    EmployeeService employeeService;

    @Autowired
    ResponseGenerator responseGenerator;


    @PostMapping(value ="", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> generatePaymentOrders(@RequestBody CreateEmployeeDto employeeDto) throws IOException {
        employeeService.createEmployee(employeeDto);
        return responseGenerator.generateAcceptedResponse(employeeDto, HttpStatus.ACCEPTED);
    }



}
