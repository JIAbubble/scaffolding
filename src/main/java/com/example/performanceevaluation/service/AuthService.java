package com.example.performanceevaluation.service;

import com.example.performanceevaluation.pojo.LoginResult;
import com.example.performanceevaluation.pojo.User;

/**
 * 认证服务接口
 */
public interface AuthService {
    
    /**
     * 用户注册
     * 
     * @param username 用户名
     * @param password 密码
     * @param email 邮箱
     * @return 用户信息
     */
    User register(String username, String password, String email);
    
    /**
     * 用户登录
     * 
     * @param username 用户名
     * @param password 密码
     * @return 登录结果（包含Token和用户信息）
     */
    LoginResult login(String username, String password);
    
    /**
     * 用户登出
     * 
     * @param userId 用户ID
     */
    void logout(Long userId);
    
    /**
     * 刷新Token
     * 
     * @param userId 用户ID
     * @return 新的Token
     */
    String refreshToken(Long userId);
    
    /**
     * 根据ID获取用户信息
     * 
     * @param userId 用户ID
     * @return 用户信息
     */
    User getUserById(Long userId);
}

