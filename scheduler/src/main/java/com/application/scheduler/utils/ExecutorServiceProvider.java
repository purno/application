package com.application.scheduler.utils;

import com.application.scheduler.config.CronConfig;
import com.application.scheduler.cron.CronType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.instrument.async.TraceableExecutorService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class ExecutorServiceProvider {


	
	public Map<CronType, ExecutorService> cronTypeVsExecutorMap = new HashMap<CronType, ExecutorService>();

	@Autowired
	BeanFactory beanFactory;
	
	@Autowired
    CronConfig cronConfig;

	@PostConstruct
	public void init() {
		Arrays.stream(CronType.values())
			.forEach(cronType -> cronTypeVsExecutorMap.put(cronType, new TraceableExecutorService(beanFactory, getExecutorService(cronType))));
		log.info("created pool of executors: " + cronTypeVsExecutorMap);
	}

	private ExecutorService getExecutorService(CronType cronType) {
		
		CronConfig.ExecutorConfig executorConfig;
		
		switch (cronType) {
			case CRON_TYPE_SYNC:
			executorConfig = cronConfig.getSyncGeoLocationCronConfig().getExecutorConfig();
			return new ThreadPoolExecutor(executorConfig.getCorePoolSize(), executorConfig.getMaxPoolSize(), executorConfig.getKeepAliveTime(), TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(executorConfig.getMaxPoolSize()));
		default:
			throw new RuntimeException("case not handled for crontype: " + cronType);
			
		}
		
	}

	public ExecutorService getExecutor (CronType cronType){
		return cronTypeVsExecutorMap.get(cronType);
	}

}
