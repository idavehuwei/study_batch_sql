package com.esoon.study.batch.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class BatchSqlService {
    private static final Logger logger = LoggerFactory.getLogger(BatchSqlService.class);

    @Value("${batch.size:1000}")
    private int batchSize;

    @Value("${batch.sql.maxLength:1000000}")
    private int maxSqlLength;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 批量插入数据
     */
    @Transactional
    public <T> void batchInsert(List<T> entities, TableMetaHandler<T> metaHandler) {
        if (entities == null || entities.isEmpty()) {
            return;
        }

        long startTime = System.currentTimeMillis();
        int totalSize = entities.size();
        int processedCount = 0;

        logger.info("开始批量插入数据, 总记录数: {}, 批次大小: {}", totalSize, batchSize);

        try {
            while (processedCount < totalSize) {
                int currentBatchSize = Math.min(batchSize, totalSize - processedCount);
                List<T> batchEntities = entities.subList(processedCount, processedCount + currentBatchSize);

                String sql = generateBatchInsertSql(batchEntities, metaHandler);
                executeBatch(sql, currentBatchSize);

                processedCount += currentBatchSize;
                logger.info("已处理: {}/{} 条记录", processedCount, totalSize);
            }

            long endTime = System.currentTimeMillis();
            logger.info("批量插入完成, 总耗时: {} ms, 平均每条记录耗时: {} ms",
                    endTime - startTime, (endTime - startTime) / (float)totalSize);

        } catch (Exception e) {
            logger.error("批量插入失败", e);
            throw new RuntimeException("批量插入失败", e);
        }
    }

    private <T> String generateBatchInsertSql(List<T> entities, TableMetaHandler<T> metaHandler) {
        StringBuilder sqlBuilder = new StringBuilder(maxSqlLength);

        // 构建INSERT语句头部
        sqlBuilder.append("INSERT INTO ")
                .append(metaHandler.getTableName())
                .append(" (")
                .append(String.join(",", metaHandler.getColumnNames()))
                .append(") VALUES ");

        // 添加每条记录的值
        for (int i = 0; i < entities.size(); i++) {
            if (i > 0) {
                sqlBuilder.append(",");
            }
            sqlBuilder.append(metaHandler.toValueString(entities.get(i)));
        }

        return sqlBuilder.toString();
    }

    private void executeBatch(String sql, int batchSize) {
        try {
            long start = System.currentTimeMillis();
            jdbcTemplate.update(sql);
            long end = System.currentTimeMillis();
            logger.debug("执行批次 SQL, 记录数: {}, 耗时: {} ms", batchSize, end - start);
        } catch (Exception e) {
            logger.error("执行批次SQL失败, 批次大小: " + batchSize, e);
            throw new RuntimeException("执行批次SQL失败", e);
        }
    }
}