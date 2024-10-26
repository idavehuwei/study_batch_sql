package com.esoon.study.batch.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 表元数据处理器抽象基类
 * @param <T> 实体类型
 */
public abstract class AbstractTableMetaHandler<T> implements TableMetaHandler<T> {

    protected static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取字段映射列表
     */
    protected abstract List<TableFieldMapping<T>> getFieldMappings();

    @Override
    public List<String> getColumnNames() {
        return getFieldMappings().stream()
                .map(TableFieldMapping::getColumnName)
                .toList();
    }

    @Override
    public String toValueString(T entity) {
        StringBuilder sb = new StringBuilder(1000);
        sb.append('(');

        List<TableFieldMapping<T>> mappings = getFieldMappings();
        for (int i = 0; i < mappings.size(); i++) {
            if (i > 0) {
                appendComma(sb);
            }
            Object value = mappings.get(i).getGetter().apply(entity);
            appendValue(sb, value);
        }

        sb.append(')');
        return sb.toString();
    }

    protected void appendComma(StringBuilder sb) {
        sb.append(',');
    }

    protected void appendValue(StringBuilder sb, Object value) {
        if (value == null) {
            sb.append("NULL");
        } else if (value instanceof String) {
            appendString(sb, (String) value);
        } else if (value instanceof Date) {
            appendDateTime(sb, (Date) value);
        } else if (value instanceof Number) {
            sb.append(value);
        } else if (value instanceof Boolean) {
            sb.append((Boolean) value ? 1 : 0);
        } else {
            appendString(sb, value.toString());
        }
    }

    protected void appendString(StringBuilder sb, String value) {
        sb.append('\'').append(escapeString(value)).append('\'');
    }

    protected void appendDateTime(StringBuilder sb, Date date) {
        sb.append('\'').append(DATE_FORMAT.format(date)).append('\'');
    }

    protected String escapeString(String str) {
        if (str == null) return "";
        return str.replace("'", "''")
                .replace("\\", "\\\\");
    }
}