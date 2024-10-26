package com.esoon.study.batch.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.esoon.study.batch.mapper.CallInDetailMapper;
import com.esoon.study.batch.entity.CallInDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class CallInDetailService extends ServiceImpl<CallInDetailMapper, CallInDetail> {

    private static final Logger logger = LoggerFactory.getLogger(CallInDetailService.class);

    @Transactional(rollbackFor = Exception.class)
    public void generateAndInsertBatchData(int batchSize) {
        long startTime = System.currentTimeMillis();
        int totalRound = (batchSize + 999) / 1000; // 计算总轮次
        List<CallInDetail> batch = new ArrayList<>(batchSize);

        for (int i = 0; i < batchSize; i++) {
            long singleStartTime = System.currentTimeMillis();
            CallInDetail detail = generateRandomDetail();
            batch.add(detail);
            long singleEndTime = System.currentTimeMillis();
            logger.info("生成第 {} 条数据，轮次: {}/{}, 耗时: {} 毫秒", i + 1, (i / 1000) + 1, totalRound, singleEndTime - singleStartTime);

            // 每1000条执行一次批量插入
            if (batch.size() >= 1000) {
                long insertStartTime = System.currentTimeMillis();
                saveBatch(batch, 1000);
                batch.clear();
                long insertEndTime = System.currentTimeMillis();
                logger.info("第 {} 轮插入1000条数据完成，总轮次: {}/{}, 插入耗时: {} 毫秒", (i / 1000) + 1, (i / 1000) + 1, totalRound, insertEndTime - insertStartTime);
            }
        }

        // 处理剩余的数据
        if (!batch.isEmpty()) {
            long insertStartTime = System.currentTimeMillis();
            saveBatch(batch, 1000);
            long insertEndTime = System.currentTimeMillis();
            logger.info("最后一轮插入 {} 条数据完成，总轮次: {}/{}, 插入耗时: {} 毫秒", batch.size(), totalRound, totalRound, insertEndTime - insertStartTime);
        }

        long endTime = System.currentTimeMillis();
        logger.info("生成并插入 {} 条数据完成，总耗时: {} 毫秒", batchSize, endTime - startTime);
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
        detail.setCityId(String.valueOf(1));
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