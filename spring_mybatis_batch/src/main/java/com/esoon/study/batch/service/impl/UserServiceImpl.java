package com.esoon.study.batch.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.esoon.study.batch.entity.User;
import com.esoon.study.batch.mapper.UserMapper;
import com.esoon.study.batch.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private List<User> userList = new ArrayList<>();

    @Override
    public void saveUser(User user) {
        userList.add(user);
    }

    @Override
    public List<User> getUserList() {
        return userList;
    }
}