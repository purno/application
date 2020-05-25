package com.application.integration.service;

import com.application.integration.dto.request.CreatePayrollRecordRequestDto;
import com.application.integration.dto.response.CreatePayrollInfoResponseDto;
import com.application.integration.dto.response.GetPayrollInfoResponseDto;
import com.application.integration.exceptions.IntegrationException;

public interface PayrollIntegrationService {

    /**
     * the below api is used to create a payroll record
     * <p>
     * {@link   "http://dummy.restapiexample.com/create" }
     * </p>
     * @param recordDto : the request in the format {@link CreatePayrollRecordRequestDto}
     * @return {@link CreatePayrollInfoResponseDto}
     * @throws IntegrationException
     */
    CreatePayrollInfoResponseDto createPayrollRecord(CreatePayrollRecordRequestDto recordDto) throws IntegrationException;


    /**
     * the below api is used to fetch data from payroll system
     * <p>
     * {@link "http://dummy.restapiexample.com/employee"}
     * </p>
     * @param payrollEmployeeId : the payrollId of the employee
     * @return {@link GetPayrollInfoResponseDto}
     * @throws IntegrationException
     */
    GetPayrollInfoResponseDto getPayrollRecord(String payrollEmployeeId) throws IntegrationException;
}
