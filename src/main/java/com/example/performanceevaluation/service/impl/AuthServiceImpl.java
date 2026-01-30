package com.example.performanceevaluation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.performanceevaluation.constants.SystemConstants;
import com.example.performanceevaluation.constants.UserConstants;
import com.example.performanceevaluation.exception.BusinessException;
import com.example.performanceevaluation.pojo.LoginResult;
import com.example.performanceevaluation.pojo.ResultCode;
import com.example.performanceevaluation.pojo.User;
import com.example.performanceevaluation.service.AuthService;
import com.example.performanceevaluation.service.UserService;
import com.example.performanceevaluation.utils.JwtUtil;
import com.example.performanceevaluation.utils.RedisUtil;
import com.example.performanceevaluation.utils.SecurityUtil;
import com.example.performanceevaluation.utils.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 认证服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    
    private final UserService userService;
    private final RedisUtil redisUtil;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public User register(String username, String password, String email) {
        // 验证用户名是否已存在
        User existingUser = userService.getOne(
            new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username)
        );
        if (existingUser != null) {
            throw new BusinessException(ResultCode.DATA_EXIST, "用户名已存在");
        }
        
        // 验证邮箱是否已存在
        if (email != null && !email.isEmpty()) {
            existingUser = userService.getOne(
                new LambdaQueryWrapper<User>()
                    .eq(User::getEmail, email)
            );
            if (existingUser != null) {
                throw new BusinessException(ResultCode.DATA_EXIST, "邮箱已被注册");
            }
        }
        
        // 验证密码强度
        if (!ValidationUtil.isStrongPassword(password)) {
            throw new BusinessException(ResultCode.PARAM_VALIDATE_ERROR, "密码强度不够，至少包含大小写字母和数字");
        }
        
        // 生成盐值
        SecurityUtil.generateRandomString(16);
        
        // 加密密码
        String hashedPassword = SecurityUtil.hashPassword(password, SecurityUtil.generateRandomString(16));
        
        // 创建用户
        User user = User.builder()
                .username(username)
               // .password(hashedPassword)
                .password(password)
                .email(email)
                .role(UserConstants.ROLE_USER)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        userService.save(user);
        
        log.info("用户注册成功: {}", username);
        return user;
    }
    
    @Override
    public LoginResult login(String username, String password) {
        // 查询用户
        User user = userService.getOne(
            new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username)
        );
        
        if (user == null) {
            throw new BusinessException(ResultCode.DATA_NOT_EXIST, "用户名或密码错误");
        }
        
        // 验证密码
      //  boolean isValid = SecurityUtil.verifyPassword(password, SecurityUtil.generateRandomString(16), user.getPassword());
        boolean isValid = password.equals(user.getPassword());
        if (!isValid) {
            throw new BusinessException(ResultCode.UNAUTHORIZED, "用户名或密码错误");
        }
        
        // 生成Token
        String token = JwtUtil.generateToken(user.getId(), user.getUsername());
        
        // 存储Token到Redis
        redisUtil.setToken(user.getId(), token);
        
        // 构建登录结果
        LoginResult result = LoginResult.builder()
                .token(token)
                .user(user)
                .expireTime(SystemConstants.TOKEN_EXPIRE_TIME)
                .build();
        
        log.info("用户登录成功: {}", username);
        return result;
    }
    
    @Override
    public void logout(Long userId) {
        redisUtil.deleteToken(userId);
        log.info("用户登出: {}", userId);
    }
    
    @Override
    public String refreshToken(Long userId) {
        User user = userService.getById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.DATA_NOT_EXIST, "用户不存在");
        }
        
        // 生成新Token
        String newToken = JwtUtil.generateToken(user.getId(), user.getUsername());
        
        // 更新Redis中的Token
        redisUtil.setToken(user.getId(), newToken);
        
        log.info("Token刷新成功: {}", userId);
        return newToken;
    }
    
    @Override
    public User getUserById(Long userId) {
        return userService.getById(userId);
    }
}

