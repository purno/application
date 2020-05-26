package com.application.web.controller;

import com.application.commons.utils.ResponseGenerator;
import com.application.dao.entities.Employee;
import com.application.service.EmployeeService;
import com.application.service.dto.request.CreateEmployeeDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

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

    /**
     * since the name of the person is unique then getting that information via path variable
     * @param employeeName
     * @return
     */

    @GetMapping(value = "/{employeeName}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> fetchRecords(@PathVariable String employeeName,
                                               @RequestParam(required = false) int age){
        List<Employee> employeeList = employeeService.fetchEmployees(employeeName, age, null, null, PageRequest.of(0,1));
        return responseGenerator.generateSuccessResponse(employeeList, HttpStatus.OK);
    }

    /**
     * for searching the employees on basis of alias and list of employeeIds and age
     * @param alias
     * @param employeeId
     * @param age
     * @return
     */
    @GetMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> fetchRecordsGeneral(@RequestParam(required = false) String alias,
                                               @RequestParam(required = false) List<String> employeeId,
                                               @RequestParam(required = false) int age){
        List<Employee> employeeList = employeeService.fetchEmployees(null, age, CollectionUtils.isEmpty(employeeId) ? null : employeeId, alias, PageRequest.of(0,10));
        return responseGenerator.generateSuccessResponse(employeeList, HttpStatus.OK);
    }

}
