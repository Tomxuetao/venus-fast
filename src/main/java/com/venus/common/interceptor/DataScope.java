package com.venus.common.interceptor;

import lombok.Data;

@Data
public class DataScope {
    private String sqlFilter;

    public DataScope(String sqlFilter) {
        this.sqlFilter = sqlFilter;
    }

    @Override
    public String toString() {
        return this.sqlFilter;
    }
}
