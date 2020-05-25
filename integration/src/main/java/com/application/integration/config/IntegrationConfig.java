package com.application.integration.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@ConfigurationProperties("integration-config")
@Getter
@Setter
@ToString
@PropertySources({
        @PropertySource(value = { "classpath:integration-${spring.profiles.active}.properties" }),
        /*
         * the below file is added so as to provide a consolidated application properties file to the QA
         */
        @PropertySource(value = "classpath:application-configuration.properties", ignoreResourceNotFound = true)
})
@Slf4j
public class IntegrationConfig implements InitializingBean {

    private SampleConfig sampleConfig;


    @Setter
    @Getter
    @ToString
    public static class SampleConfig {
        private String url;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("Integration Properties ==> " + this.toString());
    }



}
