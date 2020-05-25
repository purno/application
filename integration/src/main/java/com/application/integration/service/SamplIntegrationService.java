package com.application.integration.service;

import com.application.integration.exceptions.IntegrationException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import java.io.IOException;

/**
 * @author miggy_p
 * @project interview
 */
public interface SamplIntegrationService {
    @Retryable(exclude = {IllegalArgumentException.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000, multiplier = 1))
    @Cacheable(value = "sampleIntegrationCache", unless = "#result == null")
    String getSampleApiDetails(String address) throws IntegrationException, IOException;
}
