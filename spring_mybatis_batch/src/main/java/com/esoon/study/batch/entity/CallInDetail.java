package com.esoon.study.batch.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_cms2_callin_manual_detail")
public class CallInDetail {
    @TableId("id")
    private String id;

    @TableField("call_id")
    private String callId;

    @TableField("log_date")
    private Date logDate;

    @TableField(exist = false)
    private Date logQuarter;

    @TableField(exist = false)
    private Date logHalf;

    @TableField(exist = false)
    private Date logHour;

    @TableField(exist = false)
    private Date logDay;

    @TableField("caller_no")
    private String callerNo;

    @TableField("callee_no")
    private String calleeNo;

    @TableField("wait_begin")
    private Date waitBegin;

    @TableField("ack_begin")
    private Date ackBegin;

    @TableField("call_begin")
    private Date callBegin;

    @TableField("call_end")
    private Date callEnd;

    @TableField("call_type")
    private int callType;

    @TableField("net_ent_id")
    private int netEntId;

    @TableField("vdn")
    private int vdn;

    @TableField("service_no")
    private int serviceNo;

    @TableField("org_cc_no")
    private int orgCcNo;

    @TableField("sub_cc_no")
    private int subCcNo;

    @TableField("ser_cc_no")
    private int serCcNo;

    @TableField("org_agent_id")
    private int orgAgentId;

    @TableField("ser_agent_id")
    private int serAgentId;

    @TableField("current_agent_id")
    private int currentAgentId;

    @TableField("org_skill_id")
    private int orgSkillId;

    @TableField("current_skill_id")
    private int currentSkillId;

    @TableField("city_id")
    private String cityId;

    @TableField("user_level")
    private int userLevel;

    @TableField("wait_cause")
    private int waitCause;

    @TableField("release_cause")
    private int releaseCause;

    @TableField("leave_reason")
    private int leaveReason;

    @TableField("call_id_count")
    private int callIdCount;

    @TableField("wait_ans_num")
    private int waitAnsNum;

    @TableField("wait_ans_time")
    private int waitAnsTime;

    @TableField("succ_wait_ans_time")
    private int succWaitAnsTime;

    @TableField("no_ack_num")
    private int noAckNum;

    @TableField("succ_queue_wait_time")
    private int succQueueWaitTime;

    @TableField("succ_queue_wait_num")
    private int succQueueWaitNum;

    @TableField("fail_queue_wait_time")
    private int failQueueWaitTime;

    @TableField("fail_queue_wait_num")
    private int failQueueWaitNum;

    @TableField("in_occupy_num")
    private int inOccupyNum;

    @TableField("in_call_succ_num")
    private int inCallSuccNum;

    @TableField("in_call_time")
    private int inCallTime;

    @TableField("que_over_to_ivr_num")
    private int queOverToIvrNum;

    @TableField("que_over_to_ivr_time")
    private int queOverToIvrTime;

    @TableField("hang_up_num")
    private int hangUpNum;

    @TableField("hang_up_time")
    private int hangUpTime;

    @TableField("hang_up_release_num")
    private int hangUpReleaseNum;

    @TableField("ivr_occupy_num")
    private int ivrOccupyNum;

    @TableField("ivr_call_succ_num")
    private int ivrCallSuccNum;

    @TableField("ivr_call_time")
    private int ivrCallTime;

    @TableField("succ_wait_ans_num")
    private int succWaitAnsNum;

    @TableField("fail_wait_ans_time")
    private int failWaitAnsTime;

    @TableField("queue_wait_time")
    private int queueWaitTime;

    @TableField("ivr_only_num")
    private int ivrOnlyNum;

    @TableField("ivr_only_succ_num")
    private int ivrOnlySuccNum;

    @TableField("ivr_only_time")
    private int ivrOnlyTime;

    @TableField("net_call_num")
    private int netCallNum;

    @TableField("net_call_succ_num")
    private int netCallSuccNum;

    @TableField("auto_call_num")
    private int autoCallNum;

    @TableField("auto_ans_time")
    private int autoAnsTime;

    @TableField("auto_call_succ_num")
    private int autoCallSuccNum;

    @TableField("auto_transfer_num")
    private int autoTransferNum;

    @TableField("auto_user_abandon_num")
    private int autoUserAbandonNum;

    @TableField("in_vdn_time")
    private int inVdnTime;

    @TableField("agent_to_agent_num")
    private int agentToAgentNum;

    @TableField("agent_to_queue_num")
    private int agentToQueueNum;

    @TableField("anony_call")
    private int anonyCall;

    @TableField("media_type")
    private int mediaType;

    @TableField("sub_media_type")
    private int subMediaType;

    @TableField("UPDATE_TIME")
    private Date updateTime;

    @TableField("ivr_occupy_unique_num")
    private int ivrOccupyUniqueNum;

    @TableField("ivr_call_succ_unique_num")
    private int ivrCallSuccUniqueNum;

    @TableField(exist = false)
    private int spidCallTime;

    @TableField(exist = false)
    private int queTotalNum;

    @TableField(exist = false)
    private int queTotalQueTime;

    @TableField(exist = false)
    private int queTotalCallTime;

    @TableField(exist = false)
    private int ivrShortCallNum;

    @TableField("in_call_cost_minute")
    private int inCallCostMinute;

    @TableField("in_call_all_cost_minute")
    private int inCallAllCostMinute;

    @TableField("system_in_bound")
    private int systemInBound;

    @TableField("system_in_succ")
    private int systemInSucc;

    @TableField("user_abandon_queue_num")
    private int userAbandonQueueNum;

    @TableField("ivr_to_agent_num")
    private int ivrToAgentNum;

    @TableField("VOICE_CALL_TYPE")
    private int voiceCallType;

    @TableField("acr_2")
    private int acr2;

    @TableField("acr_8")
    private int acr8;

    @TableField("acr_12")
    private int acr12;

    @TableField("acr_24")
    private int acr24;

    @TableField("acr_48")
    private int acr48;

    @TableField(exist = false)
    private int localLogWeek;
}