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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class CallInDetailBatchService {
    private static final Logger logger = LoggerFactory.getLogger(CallInDetailBatchService.class);
    private static final int BATCH_SIZE = 50;

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
                insertBatchWithValues(batch);
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

    private void insertBatchWithValues(List<CallInDetail> batch) {
        String sql = "INSERT INTO t_cms2_callin_manual_detail (" +
                String.join(",", SQL_FIELD_NAMES) +
                ") VALUES (" +
                String.join(",", Collections.nCopies(SQL_FIELD_NAMES.size(), "?")) +
                ")";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                CallInDetail detail = batch.get(i);
                int paramIndex = 1;

                // ID和基本信息
                ps.setString(paramIndex++, detail.getId());
                ps.setString(paramIndex++, detail.getCallId());
                ps.setTimestamp(paramIndex++, new Timestamp(detail.getLogDate().getTime()));
                ps.setString(paramIndex++, detail.getCallerNo());
                ps.setString(paramIndex++, detail.getCalleeNo());

                // 时间字段
                ps.setTimestamp(paramIndex++, detail.getWaitBegin() != null ? new Timestamp(detail.getWaitBegin().getTime()) : null);
                ps.setTimestamp(paramIndex++, detail.getAckBegin() != null ? new Timestamp(detail.getAckBegin().getTime()) : null);
                ps.setTimestamp(paramIndex++, detail.getCallBegin() != null ? new Timestamp(detail.getCallBegin().getTime()) : null);
                ps.setTimestamp(paramIndex++, detail.getCallEnd() != null ? new Timestamp(detail.getCallEnd().getTime()) : null);

                // 数值字段
                ps.setInt(paramIndex++, detail.getCallType());
                ps.setInt(paramIndex++, detail.getVdn());
                ps.setInt(paramIndex++, detail.getNetEntId());
                ps.setInt(paramIndex++, detail.getServiceNo());
                ps.setInt(paramIndex++, detail.getOrgCcNo());
                ps.setInt(paramIndex++, detail.getSubCcNo());
                ps.setInt(paramIndex++, detail.getSerCcNo());
                ps.setInt(paramIndex++, detail.getOrgAgentId());
                ps.setInt(paramIndex++, detail.getSerAgentId());
                ps.setInt(paramIndex++, detail.getCurrentAgentId());
                ps.setInt(paramIndex++, detail.getOrgSkillId());
                ps.setInt(paramIndex++, detail.getCurrentSkillId());

                // City ID 需要特殊处理
                ps.setString(paramIndex++, String.format("%02d", Integer.parseInt(detail.getCityId())));

                // 其他数值字段
                ps.setInt(paramIndex++, detail.getUserLevel());
                ps.setInt(paramIndex++, detail.getWaitCause());
                ps.setInt(paramIndex++, detail.getReleaseCause());
                ps.setInt(paramIndex++, detail.getLeaveReason());
                ps.setInt(paramIndex++, detail.getCallIdCount());
                ps.setInt(paramIndex++, detail.getWaitAnsNum());
                ps.setInt(paramIndex++, detail.getWaitAnsTime());
                ps.setInt(paramIndex++, detail.getSuccWaitAnsTime());
                ps.setInt(paramIndex++, detail.getNoAckNum());
                ps.setInt(paramIndex++, detail.getSuccQueueWaitTime());
                ps.setInt(paramIndex++, detail.getSuccQueueWaitNum());
                ps.setInt(paramIndex++, detail.getFailQueueWaitTime());
                ps.setInt(paramIndex++, detail.getFailQueueWaitNum());
                ps.setInt(paramIndex++, detail.getInOccupyNum());
                ps.setInt(paramIndex++, detail.getInCallSuccNum());
                ps.setInt(paramIndex++, detail.getInCallTime());
                ps.setInt(paramIndex++, detail.getQueOverToIvrNum());
                ps.setInt(paramIndex++, detail.getQueOverToIvrTime());
                ps.setInt(paramIndex++, detail.getHangUpNum());
                ps.setInt(paramIndex++, detail.getHangUpTime());
                ps.setInt(paramIndex++, detail.getHangUpReleaseNum());
                ps.setInt(paramIndex++, detail.getIvrOccupyNum());
                ps.setInt(paramIndex++, detail.getIvrCallSuccNum());
                ps.setInt(paramIndex++, detail.getIvrCallTime());
                ps.setInt(paramIndex++, detail.getFailWaitAnsTime());
                ps.setInt(paramIndex++, detail.getQueueWaitTime());
                ps.setInt(paramIndex++, detail.getIvrOnlyNum());
                ps.setInt(paramIndex++, detail.getIvrOnlySuccNum());
                ps.setInt(paramIndex++, detail.getIvrOnlyTime());
                ps.setInt(paramIndex++, detail.getNetCallNum());
                ps.setInt(paramIndex++, detail.getNetCallSuccNum());
                ps.setInt(paramIndex++, detail.getAutoCallNum());
                ps.setInt(paramIndex++, detail.getAutoAnsTime());
                ps.setInt(paramIndex++, detail.getAutoCallSuccNum());
                ps.setInt(paramIndex++, detail.getAutoTransferNum());
                ps.setInt(paramIndex++, detail.getAutoUserAbandonNum());
                ps.setInt(paramIndex++, detail.getInVdnTime());
                ps.setInt(paramIndex++, detail.getAgentToAgentNum());
                ps.setInt(paramIndex++, detail.getAgentToQueueNum());
                ps.setInt(paramIndex++, detail.getSuccWaitAnsNum());
                ps.setInt(paramIndex++, detail.getInCallCostMinute());
                ps.setInt(paramIndex++, detail.getUserAbandonQueueNum());
                ps.setInt(paramIndex++, detail.getAnonyCall());
                ps.setInt(paramIndex++, detail.getIvrToAgentNum());
                ps.setInt(paramIndex++, detail.getSystemInBound());
                ps.setInt(paramIndex++, detail.getSystemInSucc());
                ps.setInt(paramIndex++, detail.getMediaType());
                ps.setInt(paramIndex++, detail.getSubMediaType());

                // 更新时间
                ps.setTimestamp(paramIndex++, new Timestamp(detail.getUpdateTime().getTime()));

                // 剩余数值字段
                ps.setInt(paramIndex++, detail.getIvrOccupyUniqueNum());
                ps.setInt(paramIndex++, detail.getIvrCallSuccUniqueNum());
                ps.setInt(paramIndex++, detail.getInCallAllCostMinute());
                ps.setInt(paramIndex++, detail.getVoiceCallType());
                ps.setInt(paramIndex++, detail.getAcr2());
                ps.setInt(paramIndex++, detail.getAcr8());
                ps.setInt(paramIndex++, detail.getAcr12());
                ps.setInt(paramIndex++, detail.getAcr24());
                ps.setInt(paramIndex++, detail.getAcr48());
            }

            @Override
            public int getBatchSize() {
                return batch.size();
            }
        });
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