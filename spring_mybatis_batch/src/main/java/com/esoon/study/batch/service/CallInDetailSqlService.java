package com.esoon.study.batch.service;

import com.esoon.study.batch.entity.CallInDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class CallInDetailSqlService {
    private static final Logger logger = LoggerFactory.getLogger(CallInDetailSqlService.class);
    private static final int BATCH_SIZE = 1000;

    // SQL字段名列表，用于日志调试
    private static final List<String> SQL_FIELD_NAMES = Arrays.asList(
            "id", "call_id", "log_date", "caller_no", "callee_no",
            "wait_begin", "ack_begin", "call_begin", "call_end",
            "call_type", "vdn", "net_ent_id", "service_no", "org_cc_no",
            "sub_cc_no", "ser_cc_no", "org_agent_id", "ser_agent_id",
            "current_agent_id", "org_skill_id", "current_skill_id",
            "city_id", "user_level", "wait_cause", "release_cause",
            "leave_reason", "call_id_count", "wait_ans_num", "wait_ans_time",
            "succ_wait_ans_time", "no_ack_num", "succ_queue_wait_time",
            "succ_queue_wait_num", "fail_queue_wait_time", "fail_queue_wait_num",
            "in_occupy_num", "in_call_succ_num", "in_call_time",
            "que_over_to_ivr_num", "que_over_to_ivr_time", "hang_up_num",
            "hang_up_time", "hang_up_release_num", "ivr_occupy_num",
            "ivr_call_succ_num", "ivr_call_time", "fail_wait_ans_time",
            "queue_wait_time", "ivr_only_num", "ivr_only_succ_num",
            "ivr_only_time", "net_call_num", "net_call_succ_num",
            "auto_call_num", "auto_ans_time", "auto_call_succ_num",
            "auto_transfer_num", "auto_user_abandon_num", "in_vdn_time",
            "agent_to_agent_num", "agent_to_queue_num", "succ_wait_ans_num",
            "in_call_cost_minute", "user_abandon_queue_num", "anony_call",
            "ivr_to_agent_num", "system_in_bound", "system_in_succ",
            "media_type", "sub_media_type", "UPDATE_TIME",
            "ivr_occupy_unique_num", "ivr_call_succ_unique_num",
            "in_call_all_cost_minute", "VOICE_CALL_TYPE",
            "acr_2", "acr_8", "acr_12", "acr_24", "acr_48"
    );

    private static final String INSERT_SQL = "INSERT INTO t_cms2_callin_manual_detail (" +
            String.join(", ", SQL_FIELD_NAMES) +
            ") VALUES (" +
            String.join(", ", Collections.nCopies(SQL_FIELD_NAMES.size(), "?")) +
            ")";



    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Transactional
    public void generateAndInsertBatchData(int totalSize) {
        long startTime = System.currentTimeMillis();
        int totalBatches = (totalSize + BATCH_SIZE - 1) / BATCH_SIZE;

        logger.info("开始批量插入数据:");
        logger.info("总记录数: {}", totalSize);
        logger.info("批次大小: {}", BATCH_SIZE);
        logger.info("总批次数: {}", totalBatches);

        for (int batchNum = 0; batchNum < totalBatches; batchNum++) {
            try {
                int currentBatchSize = Math.min(BATCH_SIZE, totalSize - batchNum * BATCH_SIZE);
                List<CallInDetail> batch = new ArrayList<>(currentBatchSize);

                for (int i = 0; i < currentBatchSize; i++) {
                    batch.add(generateRandomDetail());
                }

                long batchStartTime = System.currentTimeMillis();
                logger.debug("开始处理第 {}/{} 批次, 批次大小: {}", batchNum + 1, totalBatches, currentBatchSize);

                insertBatchWithJdbcTemplate(batch);

                long batchEndTime = System.currentTimeMillis();
                logger.info("批次 {}/{} 插入完成，插入 {} 条记录，耗时 {} 毫秒",
                        batchNum + 1, totalBatches, currentBatchSize, batchEndTime - batchStartTime);

            } catch (Exception e) {
                logger.error("批次 {} 处理失败", batchNum + 1, e);
                throw e;
            }
        }

        long endTime = System.currentTimeMillis();
        logger.info("批量插入完成:");
        logger.info("总记录数: {}", totalSize);
        logger.info("总耗时: {} 毫秒", endTime - startTime);
        logger.info("平均每条记录耗时: {} 毫秒", (endTime - startTime) / (float) totalSize);
    }

    private void insertBatchWithJdbcTemplate(List<CallInDetail> batch) {
        try {
            jdbcTemplate.batchUpdate(INSERT_SQL, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    CallInDetail detail = batch.get(i);
                    try {
                        setParameters(ps, detail);
                    } catch (SQLException e) {
                        logger.error("Error setting parameters for record " + i + ": " + detail, e);
                        throw e;
                    }
                }

                @Override
                public int getBatchSize() {
                    return batch.size();
                }
            });
        } catch (Exception e) {
            logger.error("Error executing batch insert", e);
            if (!batch.isEmpty()) {
                logger.error("First record in the batch: " + batch.get(0));
            }
            throw e;
        }
    }

    private void setParameters(PreparedStatement ps, CallInDetail detail) throws SQLException {
        int index = 1;
        Map<String, Object> parameterValues = new LinkedHashMap<>();

        try {
            // ID和基本信息
            parameterValues.put("id", detail.getId());
            ps.setString(index++, detail.getId());

            parameterValues.put("call_id", detail.getCallId());
            ps.setString(index++, detail.getCallId());

            Timestamp logDate = detail.getLogDate() != null ? new Timestamp(detail.getLogDate().getTime()) : null;
            parameterValues.put("log_date", logDate);
            ps.setTimestamp(index++, logDate);

            parameterValues.put("caller_no", detail.getCallerNo());
            ps.setString(index++, detail.getCallerNo());

            parameterValues.put("callee_no", detail.getCalleeNo());
            ps.setString(index++, detail.getCalleeNo());

            // 时间相关字段
            Timestamp waitBegin = detail.getWaitBegin() != null ? new Timestamp(detail.getWaitBegin().getTime()) : null;
            parameterValues.put("wait_begin", waitBegin);
            ps.setTimestamp(index++, waitBegin);

            Timestamp ackBegin = detail.getAckBegin() != null ? new Timestamp(detail.getAckBegin().getTime()) : null;
            parameterValues.put("ack_begin", ackBegin);
            ps.setTimestamp(index++, ackBegin);

            Timestamp callBegin = detail.getCallBegin() != null ? new Timestamp(detail.getCallBegin().getTime()) : null;
            parameterValues.put("call_begin", callBegin);
            ps.setTimestamp(index++, callBegin);

            Timestamp callEnd = detail.getCallEnd() != null ? new Timestamp(detail.getCallEnd().getTime()) : null;
            parameterValues.put("call_end", callEnd);
            ps.setTimestamp(index++, callEnd);

            // 基本信息字段
            parameterValues.put("call_type", detail.getCallType());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getCallType()));

            parameterValues.put("vdn", detail.getVdn());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getVdn()));

            parameterValues.put("net_ent_id", detail.getNetEntId());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getNetEntId()));

            parameterValues.put("service_no", detail.getServiceNo());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getServiceNo()));

            parameterValues.put("org_cc_no", detail.getOrgCcNo());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getOrgCcNo()));

            parameterValues.put("sub_cc_no", detail.getSubCcNo());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getSubCcNo()));

            parameterValues.put("ser_cc_no", detail.getSerCcNo());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getSerCcNo()));

            // Agent相关字段
            parameterValues.put("org_agent_id", detail.getOrgAgentId());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getOrgAgentId()));

            parameterValues.put("ser_agent_id", detail.getSerAgentId());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getSerAgentId()));

            parameterValues.put("current_agent_id", detail.getCurrentAgentId());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getCurrentAgentId()));

            parameterValues.put("org_skill_id", detail.getOrgSkillId());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getOrgSkillId()));

            parameterValues.put("current_skill_id", detail.getCurrentSkillId());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getCurrentSkillId()));

            // City和用户级别
            parameterValues.put("city_id", detail.getCityId());
            ps.setString(index++, String.format("%02d", Integer.parseInt(detail.getCityId())));

            parameterValues.put("user_level", detail.getUserLevel());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getUserLevel()));

            // 状态相关字段
            parameterValues.put("wait_cause", detail.getWaitCause());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getWaitCause()));

            parameterValues.put("release_cause", detail.getReleaseCause());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getReleaseCause()));

            parameterValues.put("leave_reason", detail.getLeaveReason());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getLeaveReason()));

            // 计数器字段
            parameterValues.put("call_id_count", detail.getCallIdCount());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getCallIdCount()));

            parameterValues.put("wait_ans_num", detail.getWaitAnsNum());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getWaitAnsNum()));

            parameterValues.put("wait_ans_time", detail.getWaitAnsTime());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getWaitAnsTime()));

            // 继续设置参数...
            parameterValues.put("succ_wait_ans_time", detail.getSuccWaitAnsTime());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getSuccWaitAnsTime()));

            parameterValues.put("no_ack_num", detail.getNoAckNum());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getNoAckNum()));

            parameterValues.put("succ_queue_wait_time", detail.getSuccQueueWaitTime());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getSuccQueueWaitTime()));

            parameterValues.put("succ_queue_wait_num", detail.getSuccQueueWaitNum());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getSuccQueueWaitNum()));

            parameterValues.put("fail_queue_wait_time", detail.getFailQueueWaitTime());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getFailQueueWaitTime()));

            parameterValues.put("fail_queue_wait_num", detail.getFailQueueWaitNum());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getFailQueueWaitNum()));

            parameterValues.put("in_occupy_num", detail.getInOccupyNum());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getInOccupyNum()));

            parameterValues.put("in_call_succ_num", detail.getInCallSuccNum());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getInCallSuccNum()));

            parameterValues.put("in_call_time", detail.getInCallTime());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getInCallTime()));

            parameterValues.put("que_over_to_ivr_num", detail.getQueOverToIvrNum());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getQueOverToIvrNum()));

            parameterValues.put("que_over_to_ivr_time", detail.getQueOverToIvrTime());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getQueOverToIvrTime()));

            parameterValues.put("hang_up_num", detail.getHangUpNum());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getHangUpNum()));

            parameterValues.put("hang_up_time", detail.getHangUpTime());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getHangUpTime()));

            parameterValues.put("hang_up_release_num", detail.getHangUpReleaseNum());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getHangUpReleaseNum()));

            parameterValues.put("ivr_occupy_num", detail.getIvrOccupyNum());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getIvrOccupyNum()));

            parameterValues.put("ivr_call_succ_num", detail.getIvrCallSuccNum());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getIvrCallSuccNum()));

            parameterValues.put("ivr_call_time", detail.getIvrCallTime());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getIvrCallTime()));

            parameterValues.put("fail_wait_ans_time", detail.getFailWaitAnsTime());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getFailWaitAnsTime()));

            parameterValues.put("queue_wait_time", detail.getQueueWaitTime());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getQueueWaitTime()));

            parameterValues.put("ivr_only_num", detail.getIvrOnlyNum());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getIvrOnlyNum()));

            parameterValues.put("ivr_only_succ_num", detail.getIvrOnlySuccNum());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getIvrOnlySuccNum()));

            parameterValues.put("ivr_only_time", detail.getIvrOnlyTime());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getIvrOnlyTime()));

            parameterValues.put("net_call_num", detail.getNetCallNum());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getNetCallNum()));

            parameterValues.put("net_call_succ_num", detail.getNetCallSuccNum());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getNetCallSuccNum()));

            parameterValues.put("auto_call_num", detail.getAutoCallNum());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getAutoCallNum()));

            parameterValues.put("auto_ans_time", detail.getAutoAnsTime());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getAutoAnsTime()));

            parameterValues.put("auto_call_succ_num", detail.getAutoCallSuccNum());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getAutoCallSuccNum()));

            parameterValues.put("auto_transfer_num", detail.getAutoTransferNum());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getAutoTransferNum()));

            parameterValues.put("auto_user_abandon_num", detail.getAutoUserAbandonNum());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getAutoUserAbandonNum()));

            parameterValues.put("in_vdn_time", detail.getInVdnTime());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getInVdnTime()));

            parameterValues.put("agent_to_agent_num", detail.getAgentToAgentNum());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getAgentToAgentNum()));

            parameterValues.put("agent_to_queue_num", detail.getAgentToQueueNum());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getAgentToQueueNum()));

            parameterValues.put("succ_wait_ans_num", detail.getSuccWaitAnsNum());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getSuccWaitAnsNum()));

            parameterValues.put("in_call_cost_minute", detail.getInCallCostMinute());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getInCallCostMinute()));

            parameterValues.put("user_abandon_queue_num", detail.getUserAbandonQueueNum());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getUserAbandonQueueNum()));

            parameterValues.put("anony_call", detail.getAnonyCall());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getAnonyCall()));

            parameterValues.put("ivr_to_agent_num", detail.getIvrToAgentNum());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getIvrToAgentNum()));

            parameterValues.put("system_in_bound", detail.getSystemInBound());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getSystemInBound()));

            parameterValues.put("system_in_succ", detail.getSystemInSucc());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getSystemInSucc()));

            parameterValues.put("media_type", detail.getMediaType());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getMediaType()));

            parameterValues.put("sub_media_type", detail.getSubMediaType());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getSubMediaType()));

            Timestamp updateTime = detail.getUpdateTime() != null ? new Timestamp(detail.getUpdateTime().getTime()) : null;
            parameterValues.put("UPDATE_TIME", updateTime);
            ps.setTimestamp(index++, updateTime);

            parameterValues.put("ivr_occupy_unique_num", detail.getIvrOccupyUniqueNum());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getIvrOccupyUniqueNum()));

            parameterValues.put("ivr_call_succ_unique_num", detail.getIvrCallSuccUniqueNum());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getIvrCallSuccUniqueNum()));

            parameterValues.put("in_call_all_cost_minute", detail.getInCallAllCostMinute());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getInCallAllCostMinute()));

            parameterValues.put("VOICE_CALL_TYPE", detail.getVoiceCallType());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getVoiceCallType()));

            parameterValues.put("acr_2", detail.getAcr2());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getAcr2()));

            parameterValues.put("acr_8", detail.getAcr8());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getAcr8()));

            parameterValues.put("acr_12", detail.getAcr12());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getAcr12()));

            parameterValues.put("acr_24", detail.getAcr24());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getAcr24()));

            parameterValues.put("acr_48", detail.getAcr48());
            ps.setBigDecimal(index++, BigDecimal.valueOf(detail.getAcr48()));

        } catch (SQLException e) {
            logger.error("参数设置错误，当前索引: {}, 总参数数量: {}", index, SQL_FIELD_NAMES.size());
            logger.error("SQL字段总数: {}, 实际设置的参数数: {}", SQL_FIELD_NAMES.size(), index - 1);

            // 打印所有已设置的参数值
            logger.error("已设置的参数值:");
            parameterValues.forEach((fieldName, value) -> {
                logger.error("字段: {}, 值: {}, 类型: {}",
                        fieldName,
                        value,
                        value != null ? value.getClass().getSimpleName() : "null"
                );
            });

            // 打印未设置的参数
            if (index - 1 < SQL_FIELD_NAMES.size()) {
                logger.error("未设置的参数:");
                for (int i = index - 1; i < SQL_FIELD_NAMES.size(); i++) {
                    logger.error("缺失字段: {}", SQL_FIELD_NAMES.get(i));
                }
            }

            throw new SQLException(String.format(
                    "参数设置错误: 索引=%d, 字段名=%s, 预期参数总数=%d, 实际设置参数数=%d",
                    index,
                    index <= SQL_FIELD_NAMES.size() ? SQL_FIELD_NAMES.get(index - 1) : "未知",
                    SQL_FIELD_NAMES.size(),
                    index - 1
            ), e);
        }

        // 最终检查参数数量是否正确
        if (index - 1 != SQL_FIELD_NAMES.size()) {
            logger.error("参数数量不匹配:");
            logger.error("预期参数数: {}", SQL_FIELD_NAMES.size());
            logger.error("实际设置参数数: {}", index - 1);
            logger.error("参数值详情:");
            parameterValues.forEach((fieldName, value) -> {
                logger.error("{}: {} ({})",
                        fieldName,
                        value,
                        value != null ? value.getClass().getSimpleName() : "null"
                );
            });

            throw new IllegalStateException(String.format(
                    "参数数量不匹配。预期%d个，实际%d个",
                    SQL_FIELD_NAMES.size(),
                    index - 1
            ));
        }
    }


    private CallInDetail generateRandomDetail() {
        CallInDetail detail = new CallInDetail();
        ThreadLocalRandom random = ThreadLocalRandom.current();

        // 生成基准时间
        Date baseTime = new Date();

        detail.setId(String.valueOf(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE));
        detail.setCallId(System.currentTimeMillis() + "-" + random.nextInt(1000000));
        detail.setLogDate(baseTime);
        detail.setLogQuarter(baseTime);
        detail.setLogHalf(baseTime);
        detail.setLogHour(baseTime);
        detail.setLogDay(baseTime);
        detail.setCallerNo("1" + random.nextInt(30000000, 39999999));
        detail.setCalleeNo("" + random.nextInt(10000, 99999));

        // 设置时间序列
        detail.setWaitBegin(baseTime);
        detail.setAckBegin(new Date(baseTime.getTime() + random.nextInt(1000, 5000)));
        detail.setCallBegin(new Date(baseTime.getTime() + random.nextInt(5000, 10000)));
        detail.setCallEnd(new Date(baseTime.getTime() + random.nextInt(10000, 60000)));

        // 设置其他随机整数字段
        detail.setCallType(random.nextInt(1, 5));
        detail.setVdn(1);
        detail.setNetEntId(random.nextInt(-1, 100));
        detail.setServiceNo(random.nextInt(1, 1000));
        detail.setOrgCcNo(random.nextInt(1, 100));
        detail.setSubCcNo(random.nextInt(1, 10));
        detail.setSerCcNo(random.nextInt(1, 100));
        detail.setOrgAgentId(random.nextInt(1000, 9999));
        detail.setSerAgentId(random.nextInt(1000, 9999));
        detail.setCurrentAgentId(random.nextInt(1000, 9999));
        detail.setOrgSkillId(random.nextInt(1, 50));
        detail.setCurrentSkillId(random.nextInt(1, 50));
        detail.setCityId("01");
        detail.setUserLevel(random.nextInt(1, 5));
        detail.setWaitCause(random.nextInt(0, 10));
        detail.setReleaseCause(random.nextInt(0, 10));
        detail.setLeaveReason(random.nextInt(0, 5));

        // 设置统计相关字段
        detail.setCallIdCount(1);
        detail.setWaitAnsNum(random.nextInt(0, 5));
        detail.setWaitAnsTime(random.nextInt(0, 300));
        detail.setSuccWaitAnsTime(random.nextInt(0, 200));
        detail.setNoAckNum(random.nextInt(0, 3));
        detail.setSuccQueueWaitTime(random.nextInt(0, 500));
        detail.setSuccQueueWaitNum(random.nextInt(0, 5));
        detail.setFailQueueWaitTime(random.nextInt(0, 300));
        detail.setFailQueueWaitNum(random.nextInt(0, 3));
        detail.setInOccupyNum(random.nextInt(0, 5));
        detail.setInCallSuccNum(random.nextInt(0, 5));
        detail.setInCallTime(random.nextInt(0, 3600));
        detail.setQueOverToIvrNum(random.nextInt(0, 3));
        detail.setQueOverToIvrTime(random.nextInt(0, 300));
        detail.setHangUpNum(random.nextInt(0, 3));
        detail.setHangUpTime(random.nextInt(0, 300));
        detail.setHangUpReleaseNum(random.nextInt(0, 3));
        detail.setIvrOccupyNum(random.nextInt(0, 5));
        detail.setIvrCallSuccNum(random.nextInt(0, 5));
        detail.setIvrCallTime(random.nextInt(0, 1800));
        detail.setSuccWaitAnsNum(random.nextInt(0, 5));
        detail.setFailWaitAnsTime(random.nextInt(0, 300));
        detail.setQueueWaitTime(random.nextInt(0, 500));
        detail.setIvrOnlyNum(random.nextInt(0, 5));
        detail.setIvrOnlySuccNum(random.nextInt(0, 5));
        detail.setIvrOnlyTime(random.nextInt(0, 1800));
        detail.setNetCallNum(random.nextInt(0, 5));
        detail.setNetCallSuccNum(random.nextInt(0, 5));
        detail.setAutoCallNum(random.nextInt(0, 5));
        detail.setAutoAnsTime(random.nextInt(0, 300));
        detail.setAutoCallSuccNum(random.nextInt(0, 5));
        detail.setAutoTransferNum(random.nextInt(0, 3));
        detail.setAutoUserAbandonNum(random.nextInt(0, 3));
        detail.setInVdnTime(random.nextInt(0, 600));
        detail.setAgentToAgentNum(random.nextInt(0, 3));
        detail.setAgentToQueueNum(random.nextInt(0, 3));
        detail.setAnonyCall(random.nextInt(0, 2));
        detail.setMediaType(random.nextInt(1, 6));
        detail.setSubMediaType(random.nextInt(1, 20));
        detail.setUpdateTime(new Date());
        detail.setIvrOccupyUniqueNum(random.nextInt(0, 5));
        detail.setIvrCallSuccUniqueNum(random.nextInt(0, 5));
        detail.setInCallCostMinute(random.nextInt(0, 60));
        detail.setInCallAllCostMinute(random.nextInt(0, 120));
        detail.setSystemInBound(random.nextInt(0, 5));
        detail.setSystemInSucc(random.nextInt(0, 5));
        detail.setUserAbandonQueueNum(random.nextInt(0, 3));
        detail.setIvrToAgentNum(random.nextInt(0, 5));
        detail.setVoiceCallType(random.nextInt(1, 5));
        detail.setAcr2(random.nextInt(0, 100));
        detail.setAcr8(random.nextInt(0, 100));
        detail.setAcr12(random.nextInt(0, 100));
        detail.setAcr24(random.nextInt(0, 100));
        detail.setAcr48(random.nextInt(0, 100));
        detail.setLocalLogWeek(2);

        return detail;
    }
}