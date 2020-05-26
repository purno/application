package com.application.service.impl;

import com.application.dao.entities.Employee;
import com.application.service.dto.request.CreateEmployeeDto;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class EmployeeHelperTestImpl {


    CreateEmployeeDto createEmployeeDto;
    public Map<String , Integer> nameToCountMap = new HashMap<>();

    @BeforeEach
    public void init(){
        createEmployeeDto = new CreateEmployeeDto();
        createEmployeeDto.setName("Arvind");
        createEmployeeDto.setSalary(new BigDecimal(100));
        createEmployeeDto.setAge(10);
        nameToCountMap.put(createEmployeeDto.getName(), nameToCountMap.get(createEmployeeDto.getName()) +1);
    }



    @Test
    public void testEmployeeCreation() {
        Employee employee = new Employee();
        employee.setAge(createEmployeeDto.getAge());
        int offSetCount = nameToCountMap.get(createEmployeeDto.getName());
        employee.setName(offSetCount > 0 ? createEmployeeDto.getName() + (offSetCount+1) : createEmployeeDto.getName());
        employee.setAlias(createEmployeeDto.getName());
        nameToCountMap.put(employee.getAlias(), nameToCountMap.get(employee.getAlias()) +1);
        Assert.assertEquals(java.util.Optional.of(2), nameToCountMap.get(employee.getAlias()));
    }







}
