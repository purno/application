package com.application.integration.service.impl;


import com.application.integration.exceptions.IntegrationException;
import com.application.integration.service.SamplIntegrationService;
import com.application.integration.config.IntegrationConfig;
import com.application.commons.config.restcaller.DefaultRestCaller;
import com.application.commons.constants.IntegrationMetricConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
@Slf4j
public class SampleIntegrationServiceImpl implements SamplIntegrationService {

    @Autowired
    IntegrationConfig integrationConfig;

    IntegrationConfig.SampleConfig config;

    @PostConstruct
    public void init(){
        config = integrationConfig.getSampleConfig();
    }

    private DefaultRestCaller getCaller() {
        return new DefaultRestCaller(IntegrationMetricConstants.GOOGLE.name());
    }


    @Retryable(exclude = {IllegalArgumentException.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000, multiplier = 1))
    @Cacheable(value = "sampleIntegrationCache", unless = "#result == null")
    @Override
    public String getSampleApiDetails(String address) throws IntegrationException, IOException {
        return "SAMPLE";
    }


}
