package com.application.dao.config;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

@AllArgsConstructor
public class RoutingDataSource extends AbstractRoutingDataSource {

    DbContextHolder dbContextHolder;

    @Override
    protected Object determineCurrentLookupKey() {
        return dbContextHolder.getDbType();
    }
}
