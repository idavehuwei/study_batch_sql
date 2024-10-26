package com.esoon.study.batch.service;

import java.util.List;

public interface TableMetaHandler<T> {
    /**
     * 获取表名
     */
    String getTableName();

    /**
     * 获取列名列表
     */
    List<String> getColumnNames();

    /**
     * 将实体转换为SQL值列表
     */
    String toValueString(T entity);
}