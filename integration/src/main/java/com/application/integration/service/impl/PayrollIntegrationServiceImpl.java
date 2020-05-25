package com.application.integration.service.impl;

import com.application.commons.config.restcaller.DefaultRestCaller;
import com.application.commons.config.restcaller.HttpMethod;
import com.application.commons.constants.IntegrationMetricConstants;
import com.application.commons.utils.JsonUtils;
import com.application.integration.config.IntegrationConfig;
import com.application.integration.dto.request.CreatePayrollRecordRequestDto;
import com.application.integration.dto.response.CreatePayrollInfoResponseDto;
import com.application.integration.dto.response.GetPayrollInfoResponseDto;
import com.application.integration.exceptions.IntegrationException;
import com.application.integration.service.PayrollIntegrationService;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Objects;

@Service
@Slf4j
public class PayrollIntegrationServiceImpl implements PayrollIntegrationService {

    @Autowired
    IntegrationConfig integrationConfig;

    IntegrationConfig.PayrollConfig payrollConfig;

    @PostConstruct
    public void init() {
        payrollConfig = integrationConfig.getPayrollConfig();
    }

    private DefaultRestCaller getRestCaller(){
        return new DefaultRestCaller(IntegrationMetricConstants.PAYROLL.name());
    }

    @Retryable(
            exclude = { IllegalArgumentException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000,multiplier = 1)
    )
    @Override
    public CreatePayrollInfoResponseDto createPayrollRecord(CreatePayrollRecordRequestDto recordDto) throws IntegrationException {
        log.info(String.format(" create payroll record api url %s post body %s", payrollConfig.getCreatePayrollUrl(), recordDto));
        recordDto.validate();
        try {
            DefaultRestCaller restCaller = getRestCaller();
            restCaller.setUrl(payrollConfig.getCreatePayrollUrl());
            restCaller.setMethod(HttpMethod.POST);
            ResponseEntity<String> response = restCaller.restCall(recordDto);
            log.info("Returned with response code: {}, body: {}", response.getStatusCode(), response.getBody());
            if (HttpStatus.OK == response.getStatusCode()) {
                CreatePayrollInfoResponseDto responseDto = JsonUtils.deserialize(response.getBody(), CreatePayrollInfoResponseDto.class);
                if(!responseDto.isSuccess()){
                    throw new IntegrationException(" api not success create payroll record api -- " + response.getStatusCode() + " with body " + response.getBody());
                }
            } else {
                throw new IntegrationException("ok status not received from create payroll record api -- " + response.getStatusCode() + " with body " + response.getBody());
            }
        } catch (IOException e) {
            throw new IntegrationException("exception occurred while creating payroll record api -- " + e.getMessage(), e);
        }
        throw new IntegrationException(" Exception Occurred ");
    }

    @Retryable(
            exclude = { IllegalArgumentException.class},
            maxAttempts = 2,
            backoff = @Backoff(delay = 1000,multiplier = 1)
    )
    @Override
    public GetPayrollInfoResponseDto getPayrollRecord(String payrollEmployeeId) throws IntegrationException {
        log.info(String.format(" fetch payroll record api url %s payrollId %s",
                payrollConfig.getGetPayrollInfoUrl(), payrollEmployeeId));
        Preconditions.checkArgument(Objects.nonNull(payrollEmployeeId));
        try {
            DefaultRestCaller restCaller = getRestCaller();
            restCaller.setUrl(payrollConfig.getGetPayrollInfoUrl() + "/" + payrollEmployeeId);
            ResponseEntity<String> response = restCaller.restCall();
            if (HttpStatus.OK == response.getStatusCode()) {
                GetPayrollInfoResponseDto responseDto = JsonUtils.deserialize(response.getBody(), GetPayrollInfoResponseDto.class);
                if(!responseDto.isSuccess()){
                    throw new IntegrationException(" api not success get payroll record api -- " + response.getStatusCode() + " with body " + response.getBody());
                }
            } else {
                throw new IntegrationException("ok status not received from get payroll record api -- " + response.getStatusCode() + " with body " + response.getBody());
            }
        } catch (Exception e){
            throw new IntegrationException("exception occurred while creating payroll record api -- " + e.getMessage(), e);
        }
        throw new IntegrationException(" Exception Occurred ");
    }

}
