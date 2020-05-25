package com.application.commons.config.statsd;

import com.application.commons.config.annotation.MethodLatencyMetrics;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class MetricAspect {

    @Autowired
    private MetricUtils metricUtils;


    @Around(
            value = "@annotation(methodLatencyMetrics)",
            argNames = "joinPoint, methodLatencyMetrics")
    public Object methodExecution(ProceedingJoinPoint joinPoint, MethodLatencyMetrics actions)
            throws Throwable {

        String metricsName = actions.metricsName();
        long startTime = System.currentTimeMillis();
        Object res = joinPoint.proceed();

        long timeElapse = System.currentTimeMillis() - startTime;
        metricUtils.pushEventLatency(metricsName, timeElapse);
        log.info("method {} , executed and took {} ms", metricsName, timeElapse);
        return res;
    }
}
