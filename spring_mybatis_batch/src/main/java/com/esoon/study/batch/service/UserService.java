package com.esoon.study.batch.service;

import com.esoon.study.batch.entity.User;

import java.util.List;

public interface UserService {
    void saveUser(User user);
    List<User> getUserList();
}