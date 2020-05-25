package com.application.dao.config;

import org.springframework.stereotype.Service;

@Service
public class DbContextHolderImpl implements DbContextHolder {

    private final static ThreadLocal<DataSourceType> contextHolder = new ThreadLocal<DataSourceType>();

    public void setDbType(DataSourceType dbType) {
        if (dbType == null)
            throw new RuntimeException("DbType can't be null");
        contextHolder.set(dbType);
    }

    public DataSourceType getDbType() {
        return (DataSourceType) contextHolder.get();
    }

    public void clearDbType() {
        contextHolder.remove();
    }
}
