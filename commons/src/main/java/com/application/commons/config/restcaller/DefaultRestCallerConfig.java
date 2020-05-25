package com.application.commons.config.restcaller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@ConfigurationProperties("rest-connection-config")
@Getter
@Setter
@NoArgsConstructor
@PropertySources({
        @PropertySource(value = {"classpath:commons-${spring.profiles.active}.properties"}),
        /*
         * the below file is added so as to provide a consolidated application properties file to the QA
         */
        @PropertySource(value = "classpath:application-configuration.properties", ignoreResourceNotFound = true)
})
public class DefaultRestCallerConfig {

    private int defaultMaxConnPerRoute;
    private int maxConn;
    private int defaultSocketConfigTimeout;
    private int defaultConnectionTimeout;
    private int defaultConnectionRequestTimeout;
}