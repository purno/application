package com.application.commons.config.statsd;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration("MetricClientConfig")
@ConfigurationProperties("metric-client-config")
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
@Slf4j
@ToString
public class MetricClientConfig implements InitializingBean {
    String hostName;
    String prefix;
    int port;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info(" properties set " + this.toString());
    }
}
