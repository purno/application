package com.application.dao.config;

import javax.validation.constraints.NotNull;


public interface DbContextHolder {

    /**
     * <p>
     *
     * @return {@code null}.
     * @see DataSourceType
     */
    @NotNull
    DataSourceType getDbType();

    /**
     * <p>
     *
     * @param type
     * @throws RuntimeException
     * @see DataSourceType
     */
    void setDbType(@NotNull DataSourceType type);

    /**
     * <p>
     *
     * @see ThreadLocal
     */
    void clearDbType();
}

