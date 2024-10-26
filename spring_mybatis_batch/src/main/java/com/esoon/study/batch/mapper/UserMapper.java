package com.esoon.study.batch.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.esoon.study.batch.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}