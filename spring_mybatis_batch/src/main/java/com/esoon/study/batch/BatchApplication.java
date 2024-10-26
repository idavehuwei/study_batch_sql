package com.esoon.study.batch;

import com.esoon.study.batch.service.CallInDetailBatchService;
import com.esoon.study.batch.service.CallInDetailBatchSqlService;
import com.esoon.study.batch.service.CallInDetailService;
import com.esoon.study.batch.service.CallInDetailSqlService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.esoon.study.batch.mapper")
public class BatchApplication implements CommandLineRunner {

    private final CallInDetailService callInDetailService;
    private final CallInDetailSqlService callInDetailSqlService;
    private final CallInDetailBatchService callInDetailBatchService;
    private final CallInDetailBatchSqlService callInDetailBatchSqlService;
    public BatchApplication(CallInDetailService callInDetailService, CallInDetailSqlService callInDetailSqlService, CallInDetailBatchService callInDetailBatchService, CallInDetailBatchSqlService callInDetailBatchSqlService) {
        this.callInDetailService = callInDetailService;
        this.callInDetailSqlService = callInDetailSqlService;
        this.callInDetailBatchService = callInDetailBatchService;
        this.callInDetailBatchSqlService = callInDetailBatchSqlService;
    }

    public static void main(String[] args) {
        SpringApplication.run(BatchApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Starting data generation...");
        callInDetailBatchSqlService.generateAndInsertBatchData(100000); // 生成10万条记录
        System.out.println("Data generation completed!");
    }
}
