package com.esoon.study.batch.service;

import com.esoon.study.batch.entity.CallInDetail;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class CallInDetailMetaHandler extends AbstractTableMetaHandler<CallInDetail> {

    private static final List<TableFieldMapping<CallInDetail>> FIELD_MAPPINGS = new ArrayList<>();

    static {
        // 基本信息字段
        FIELD_MAPPINGS.add(new TableFieldMapping<>("id", CallInDetail::getId));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("call_id", CallInDetail::getCallId));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("log_date", CallInDetail::getLogDate));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("caller_no", CallInDetail::getCallerNo));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("callee_no", CallInDetail::getCalleeNo));

        // 时间相关字段
        FIELD_MAPPINGS.add(new TableFieldMapping<>("wait_begin", CallInDetail::getWaitBegin));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("ack_begin", CallInDetail::getAckBegin));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("call_begin", CallInDetail::getCallBegin));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("call_end", CallInDetail::getCallEnd));

        // 基础属性字段
        FIELD_MAPPINGS.add(new TableFieldMapping<>("call_type", CallInDetail::getCallType));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("vdn", CallInDetail::getVdn));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("net_ent_id", CallInDetail::getNetEntId));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("service_no", CallInDetail::getServiceNo));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("org_cc_no", CallInDetail::getOrgCcNo));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("sub_cc_no", CallInDetail::getSubCcNo));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("ser_cc_no", CallInDetail::getSerCcNo));

        // 座席相关字段
        FIELD_MAPPINGS.add(new TableFieldMapping<>("org_agent_id", CallInDetail::getOrgAgentId));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("ser_agent_id", CallInDetail::getSerAgentId));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("current_agent_id", CallInDetail::getCurrentAgentId));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("org_skill_id", CallInDetail::getOrgSkillId));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("current_skill_id", CallInDetail::getCurrentSkillId));

        // 其他属性字段
        FIELD_MAPPINGS.add(new TableFieldMapping<>("city_id", CallInDetail::getCityId));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("user_level", CallInDetail::getUserLevel));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("wait_cause", CallInDetail::getWaitCause));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("release_cause", CallInDetail::getReleaseCause));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("leave_reason", CallInDetail::getLeaveReason));

        // 计数统计字段
        FIELD_MAPPINGS.add(new TableFieldMapping<>("call_id_count", CallInDetail::getCallIdCount));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("wait_ans_num", CallInDetail::getWaitAnsNum));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("wait_ans_time", CallInDetail::getWaitAnsTime));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("succ_wait_ans_time", CallInDetail::getSuccWaitAnsTime));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("no_ack_num", CallInDetail::getNoAckNum));

        // 队列相关字段
        FIELD_MAPPINGS.add(new TableFieldMapping<>("succ_queue_wait_time", CallInDetail::getSuccQueueWaitTime));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("succ_queue_wait_num", CallInDetail::getSuccQueueWaitNum));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("fail_queue_wait_time", CallInDetail::getFailQueueWaitTime));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("fail_queue_wait_num", CallInDetail::getFailQueueWaitNum));

        // 呼入占用相关字段
        FIELD_MAPPINGS.add(new TableFieldMapping<>("in_occupy_num", CallInDetail::getInOccupyNum));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("in_call_succ_num", CallInDetail::getInCallSuccNum));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("in_call_time", CallInDetail::getInCallTime));

        // IVR相关字段
        FIELD_MAPPINGS.add(new TableFieldMapping<>("que_over_to_ivr_num", CallInDetail::getQueOverToIvrNum));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("que_over_to_ivr_time", CallInDetail::getQueOverToIvrTime));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("ivr_occupy_num", CallInDetail::getIvrOccupyNum));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("ivr_call_succ_num", CallInDetail::getIvrCallSuccNum));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("ivr_call_time", CallInDetail::getIvrCallTime));

        // 挂机相关字段
        FIELD_MAPPINGS.add(new TableFieldMapping<>("hang_up_num", CallInDetail::getHangUpNum));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("hang_up_time", CallInDetail::getHangUpTime));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("hang_up_release_num", CallInDetail::getHangUpReleaseNum));

        // 等待答复相关字段
        FIELD_MAPPINGS.add(new TableFieldMapping<>("fail_wait_ans_time", CallInDetail::getFailWaitAnsTime));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("queue_wait_time", CallInDetail::getQueueWaitTime));

        // IVR专属字段
        FIELD_MAPPINGS.add(new TableFieldMapping<>("ivr_only_num", CallInDetail::getIvrOnlyNum));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("ivr_only_succ_num", CallInDetail::getIvrOnlySuccNum));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("ivr_only_time", CallInDetail::getIvrOnlyTime));

        // 网络呼叫相关字段
        FIELD_MAPPINGS.add(new TableFieldMapping<>("net_call_num", CallInDetail::getNetCallNum));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("net_call_succ_num", CallInDetail::getNetCallSuccNum));

        // 自动呼叫相关字段
        FIELD_MAPPINGS.add(new TableFieldMapping<>("auto_call_num", CallInDetail::getAutoCallNum));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("auto_ans_time", CallInDetail::getAutoAnsTime));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("auto_call_succ_num", CallInDetail::getAutoCallSuccNum));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("auto_transfer_num", CallInDetail::getAutoTransferNum));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("auto_user_abandon_num", CallInDetail::getAutoUserAbandonNum));

        // VDN和座席转接相关字段
        FIELD_MAPPINGS.add(new TableFieldMapping<>("in_vdn_time", CallInDetail::getInVdnTime));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("agent_to_agent_num", CallInDetail::getAgentToAgentNum));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("agent_to_queue_num", CallInDetail::getAgentToQueueNum));

        // 成功等待相关字段
        FIELD_MAPPINGS.add(new TableFieldMapping<>("succ_wait_ans_num", CallInDetail::getSuccWaitAnsNum));

        // 通话成本相关字段
        FIELD_MAPPINGS.add(new TableFieldMapping<>("in_call_cost_minute", CallInDetail::getInCallCostMinute));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("in_call_all_cost_minute", CallInDetail::getInCallAllCostMinute));

        // 用户相关字段
        FIELD_MAPPINGS.add(new TableFieldMapping<>("user_abandon_queue_num", CallInDetail::getUserAbandonQueueNum));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("anony_call", CallInDetail::getAnonyCall));

        // IVR到座席转接字段
        FIELD_MAPPINGS.add(new TableFieldMapping<>("ivr_to_agent_num", CallInDetail::getIvrToAgentNum));

        // 系统相关字段
        FIELD_MAPPINGS.add(new TableFieldMapping<>("system_in_bound", CallInDetail::getSystemInBound));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("system_in_succ", CallInDetail::getSystemInSucc));

        // 媒体类型字段
        FIELD_MAPPINGS.add(new TableFieldMapping<>("media_type", CallInDetail::getMediaType));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("sub_media_type", CallInDetail::getSubMediaType));

        // 更新时间字段
        FIELD_MAPPINGS.add(new TableFieldMapping<>("UPDATE_TIME", CallInDetail::getUpdateTime));

        // IVR独占字段
        FIELD_MAPPINGS.add(new TableFieldMapping<>("ivr_occupy_unique_num", CallInDetail::getIvrOccupyUniqueNum));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("ivr_call_succ_unique_num", CallInDetail::getIvrCallSuccUniqueNum));

        // 语音类型字段
        FIELD_MAPPINGS.add(new TableFieldMapping<>("VOICE_CALL_TYPE", CallInDetail::getVoiceCallType));

        // ACR相关字段
        FIELD_MAPPINGS.add(new TableFieldMapping<>("acr_2", CallInDetail::getAcr2));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("acr_8", CallInDetail::getAcr8));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("acr_12", CallInDetail::getAcr12));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("acr_24", CallInDetail::getAcr24));
        FIELD_MAPPINGS.add(new TableFieldMapping<>("acr_48", CallInDetail::getAcr48));
    }

    @Override
    public String getTableName() {
        return "t_cms2_callin_manual_detail";
    }

    @Override
    protected List<TableFieldMapping<CallInDetail>> getFieldMappings() {
        return FIELD_MAPPINGS;
    }
}