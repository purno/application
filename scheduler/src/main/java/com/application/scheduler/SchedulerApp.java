package com.application.scheduler;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@ComponentScan(basePackages = {"com.*"})
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, SecurityAutoConfiguration.class})
@EnableScheduling
@Configuration
@EnableConfigurationProperties
@EntityScan("com.*")
@EnableJpaRepositories(basePackages = {"com.*"},
        entityManagerFactoryRef = "entityManagerFactory",
        enableDefaultTransactions = false)
@EnableTransactionManagement(order = 100)
public class SchedulerApp {

    public static void main(String[] args) throws UnknownHostException {
        System.setProperty("hostName", InetAddress.getLocalHost().getHostName());
        System.setProperty("applicationName", "SchedulerService");
        SpringApplication.run(SchedulerApp.class, args);
    }

}
