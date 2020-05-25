package com.application.service.helper.impl;

import com.application.commons.utils.JsonUtils;
import com.application.dao.constants.EmployeeMetaDataKeys;
import com.application.dao.entities.Employee;
import com.application.dao.jparepository.EmployeeDao;
import com.application.service.dto.request.CreateEmployeeDto;
import com.application.service.helper.EmployeeHelper;
import com.application.service.vo.PayrollInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class EmployeeHelperImpl implements EmployeeHelper {

    @Autowired
    EmployeeDao employeeDao;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Employee createAndSaveEmployee(CreateEmployeeDto createEmployeeDto) throws JsonProcessingException {
        Employee employee = createEmployee(createEmployeeDto);
       return employeeDao.save(employee);
    }

    private Employee createEmployee(CreateEmployeeDto createEmployeeDto) throws JsonProcessingException {
        Employee employee = new Employee();
        employee.setAge(createEmployeeDto.getAge());
        int offSetCount = employeeDao.getCountOfEmployeeWithSameAlias(createEmployeeDto.getName());
        employee.setName(offSetCount > 0 ? createEmployeeDto.getName() + (offSetCount+1) : createEmployeeDto.getName());
        employee.setAlias(createEmployeeDto.getName());
        Map<String, String> metaInfoMap = populateMetaInfo(createEmployeeDto);
        employee.setMetaInfo(metaInfoMap);
        return employee;
    }

    private Map<String, String> populateMetaInfo(CreateEmployeeDto createEmployeeDto) throws JsonProcessingException {
        Map<String, String> metaInfoMap = new HashMap<>();
        PayrollInfo payrollInfo = new PayrollInfo();
        payrollInfo.setSalary(String.valueOf(createEmployeeDto.getSalary()));
        metaInfoMap.put(EmployeeMetaDataKeys.PAYROLL_DATA, JsonUtils.serialize(payrollInfo));
        return metaInfoMap;
    }


}
