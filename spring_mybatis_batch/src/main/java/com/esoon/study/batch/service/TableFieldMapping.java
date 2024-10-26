package com.esoon.study.batch.service;

import java.util.function.Function;

/**
 * 表字段映射定义
 * @param <T> 实体类型
 */
public class TableFieldMapping<T> {
    private final String columnName;
    private final Function<T, Object> getter;

    public TableFieldMapping(String columnName, Function<T, Object> getter) {
        this.columnName = columnName;
        this.getter = getter;
    }

    public String getColumnName() {
        return columnName;
    }

    public Function<T, Object> getGetter() {
        return getter;
    }
}