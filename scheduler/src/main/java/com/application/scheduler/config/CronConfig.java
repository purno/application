package com.application.scheduler.config;

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
@ConfigurationProperties("cron-config")
@Getter
@Setter
@ToString
@PropertySources({
        @PropertySource(value = {"classpath:accounting-schedular-resources/schedular-cron-${spring.profiles.active}.properties"}),
        /*
         * the below file is added so as to provide a consolidated application properties file to the QA
         */
        @PropertySource(value = "classpath:application-configuration.properties", ignoreResourceNotFound = true)
})
@Slf4j
public class CronConfig implements InitializingBean {
	

	private SyncGeoLocationCronConfig syncGeoLocationCronConfig;

	@Setter
	@Getter
	@ToString
	public static class ChildCronConfig {
		private String fixedDelayInMs;
		private Integer batchSize;
		private Integer sleepTimeAfterEachIterationInMs;
		private ExecutorConfig executorConfig;
		private Integer retryCountThreshold;
		private Integer triggerTimeMultiplier;
	}
	
	@ToString(callSuper = true)
	@Setter
	@Getter
	public static class SyncGeoLocationCronConfig extends ChildCronConfig{
		
	}

	
	
	@Setter
	@Getter
	@ToString
	public static class ExecutorConfig{
		private Integer corePoolSize;
		private Integer maxPoolSize;
		private Long keepAliveTime;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		log.debug("CronConfig loaded ==> " + this.toString());
	}

}
