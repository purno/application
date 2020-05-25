package com.application.commons.config.restcaller;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author Pranav Miglani
 * @version 1.0.0
 */
@Configuration
@ConfigurationProperties("route-configs")
@Getter
@Setter
@PropertySources({
        @PropertySource(value = "classpath:routes-${spring.profiles.active}.properties", ignoreResourceNotFound = true),
        /*
         * the below file is added so as to provide a consolidated application properties file to the QA
         */
        @PropertySource(value = "classpath:application-configuration.properties", ignoreResourceNotFound = true)
})
@Slf4j
public class RouteConfigs {

    private List<RouteConfig> configs;

    @PostConstruct
    public void init() {
        configs.forEach(routeConfig -> log.info(String.format(" api properties for [%s] is [%s]", routeConfig.getHost(), routeConfig)));
    }


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class RouteConfig {
        private String host;
        private int port;
        private String scheme;
        private int maxConnections;
        private ApiTimeoutConfig hostTimeout;
        List<ApiTimeoutConfig> apiTimeout;
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApiTimeoutConfig {
        private int connectTimeout;
        private int socketTimeout;
        private int connectionRequestTimeout;
        private String endPoint;
    }


}
