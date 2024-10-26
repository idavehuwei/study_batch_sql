package com.esoon.study.batch.controller;

import com.esoon.study.batch.entity.User;
import com.esoon.study.batch.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users/create")
    public void createUser(@RequestBody User user) {
        userService.saveUser(user);
    }

    @GetMapping("/users/list")
    public List<User> getUserList() {
        return userService.getUserList();
    }
}