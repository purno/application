package com.application.dao.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;


@Aspect
@Component
@Slf4j
public class RouteQueryAdvisor implements Ordered {

    private int order;

    @Value("99")
    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Autowired
    DbContextHolder dbContextHolder;


    @Around("@annotation(com.application.dao.annotations.ReadOnly)")
    public Object aroundReadOnly(ProceedingJoinPoint pjp) throws Throwable {
        try {
            if(TransactionSynchronizationManager.isActualTransactionActive()){
                log.info(" Current txn is Active : not switching transaction : continuing with existing txn");
                return pjp.proceed();
            }
            log.info(" is switching to slave transaction ");
            DataSourceType dataSourceType = DataSourceType.SLAVE;
            dbContextHolder.setDbType(dataSourceType);
            Object res = pjp.proceed();
            dbContextHolder.clearDbType();
            return res;
        } finally {
            // restore state
            dbContextHolder.clearDbType();
        }

    }


}
