package com.application.dao.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration("DatabasePropertiesLoggingConfig")
@Slf4j
public class DatabasePropertiesLoggingConfig implements InitializingBean {
    @Autowired
    private Environment environment;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info(" ---------------------------------------  ");
        log.info(" LOGGING THE USER NAME ");
        log.info(environment.getProperty("master.datasource.hikari.username"));
        log.info(environment.getProperty("slave.datasource.hikari.username"));
        log.info(" ---------------------------------------  ");
    }
}
