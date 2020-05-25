package com.application.service.impl;

import com.application.commons.utils.JsonUtils;
import com.application.dao.constants.EmployeeMetaDataKeys;
import com.application.dao.entities.Employee;
import com.application.dao.enums.SyncStatus;
import com.application.dao.jparepository.EmployeeDao;
import com.application.integration.dto.request.CreatePayrollRecordRequestDto;
import com.application.integration.dto.response.CreatePayrollInfoResponseDto;
import com.application.integration.exceptions.IntegrationException;
import com.application.integration.service.PayrollIntegrationService;
import com.application.service.PayrollService;
import com.application.service.vo.PayrollInfo;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class PayrollServiceImpl implements PayrollService {

    @Autowired
    EmployeeDao employeeDao;

    @Autowired
    PayrollIntegrationService payrollIntegrationService;



    @Transactional(rollbackFor = Exception.class)
    @Override
    public Employee syncPayrollWithEmployeeData(String employeeId) throws IOException, IntegrationException {
        log.info(String.format(" syncing payroll for employeeId  %s ", employeeId));
        Optional<Employee> employeeOpt = employeeDao.findById(employeeId);
        Preconditions.checkArgument(employeeOpt.isPresent(), " Employee is not present for id" + employeeId);
        Employee employeeInDb = employeeOpt.get();
        Preconditions.checkArgument(employeeInDb.isSyncPending(), " The employee record is already synced !!! ");
        CreatePayrollRecordRequestDto request = createPayrollRecord(employeeInDb);
        CreatePayrollInfoResponseDto response =  payrollIntegrationService.createPayrollRecord(request) ;
        employeeInDb.setPayRollEmpId(response.getEmployeeId());
        employeeInDb.setSyncStatus(SyncStatus.SUCCESS);
       return employeeDao.save(employeeInDb);
    }


    private CreatePayrollRecordRequestDto createPayrollRecord(Employee employee) throws IOException {
        CreatePayrollRecordRequestDto record = new CreatePayrollRecordRequestDto();
        record.setAge(String.valueOf(employee.getAge()));
        record.setName(employee.getName());
        Map<String, String> metaInfoMap = employee.getMetaInfo();
        PayrollInfo payrollInfo = JsonUtils.deserialize(metaInfoMap.get(EmployeeMetaDataKeys.PAYROLL_DATA), PayrollInfo.class);
        record.setSalary(payrollInfo.getSalary());
        return record;
    }






}
