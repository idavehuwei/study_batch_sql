package com.esoon.study.batch.service;

import com.esoon.study.batch.entity.CallInDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class CallInDetailBatchSqlService {
    private static final Logger logger = LoggerFactory.getLogger(CallInDetailBatchSqlService.class);
    private static final int BATCH_SIZE = 1000;
    private static final int MAX_SQL_LENGTH = 1_000_000;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final String INSERT_PREFIX = "INSERT INTO t_cms2_callin_manual_detail ("
            + "id,call_id,log_date,caller_no,callee_no,"
            + "wait_begin,ack_begin,call_begin,call_end,"
            + "call_type,vdn,net_ent_id,service_no,org_cc_no,"
            + "sub_cc_no,ser_cc_no,org_agent_id,ser_agent_id,"
            + "current_agent_id,org_skill_id,current_skill_id,"
            + "city_id,user_level,wait_cause,release_cause,"
            + "leave_reason,call_id_count,wait_ans_num,wait_ans_time,"
            + "succ_wait_ans_time,no_ack_num,succ_queue_wait_time,"
            + "succ_queue_wait_num,fail_queue_wait_time,fail_queue_wait_num,"
            + "in_occupy_num,in_call_succ_num,in_call_time,"
            + "que_over_to_ivr_num,que_over_to_ivr_time,hang_up_num,"
            + "hang_up_time,hang_up_release_num,ivr_occupy_num,"
            + "ivr_call_succ_num,ivr_call_time,fail_wait_ans_time,"
            + "queue_wait_time,ivr_only_num,ivr_only_succ_num,"
            + "ivr_only_time,net_call_num,net_call_succ_num,"
            + "auto_call_num,auto_ans_time,auto_call_succ_num,"
            + "auto_transfer_num,auto_user_abandon_num,in_vdn_time,"
            + "agent_to_agent_num,agent_to_queue_num,succ_wait_ans_num,"
            + "in_call_cost_minute,user_abandon_queue_num,anony_call,"
            + "ivr_to_agent_num,system_in_bound,system_in_succ,"
            + "media_type,sub_media_type,UPDATE_TIME,"
            + "ivr_occupy_unique_num,ivr_call_succ_unique_num,"
            + "in_call_all_cost_minute,VOICE_CALL_TYPE,"
            + "acr_2,acr_8,acr_12,acr_24,acr_48"
            + ") VALUES ";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private BatchSqlService batchSqlService;

    @Autowired
    private CallInDetailMetaHandler metaHandler;

    @Autowired
    private CallInDetailGeneratorService generatorService;

    public void generateAndInsertBatchData(int totalSize) {
        List<CallInDetail> entities = new ArrayList<>();
        for (int i = 0; i < totalSize; i++) {
            entities.add(generatorService.generateRandomDetail());
        }

        batchSqlService.batchInsert(entities, metaHandler);
    }

}