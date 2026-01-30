package com.example.performanceevaluation.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.performanceevaluation.dao.UserMapper;
import com.example.performanceevaluation.pojo.User;
import com.example.performanceevaluation.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}

