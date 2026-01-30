package com.example.performanceevaluation.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录结果
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResult {
    
    /**
     * Token
     */
    private String token;
    
    /**
     * 用户信息
     */
    private User user;
    
    /**
     * Token过期时间（秒）
     */
    private Long expireTime;
}

