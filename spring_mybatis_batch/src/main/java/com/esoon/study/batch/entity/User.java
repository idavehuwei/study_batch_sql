package com.esoon.study.batch.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user_table")
public class User {
    private Long id;
    private String name;
    private Integer age;
}