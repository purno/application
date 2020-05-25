package com.application.commons.config.statsd;

import com.application.commons.constants.MetricConstants;
import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

@Component
@DependsOn("MetricClientConfig")
public class MetricClient {

    @Autowired
    MetricClientConfig metricClientConfig;

    @Bean
    public StatsDClient primaryStatsDClient() {
        return new NonBlockingStatsDClient(metricClientConfig.getPrefix(),     /* prefix to any stats; may be null or empty string */
                metricClientConfig.getHostName(),                        /* common case: localhost */
                metricClientConfig.getPort(),                                   /* port */
                MetricConstants.SERVICE_ID + ":" + System.getProperty("applicationName"),  /* constant tags */
                MetricConstants.SERVICE_NODE + ":" + System.getProperty("hostName")); /* constant tags */
    }
}
