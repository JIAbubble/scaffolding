package com.example.performanceevaluation.constants;

/**
 * 用户相关常量
 */
public class UserConstants {
    
    /**
     * 用户角色
     */
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_USER = "USER";
    public static final String ROLE_GUEST = "GUEST";
    
    /**
     * 用户状态
     */
    public static final int STATUS_ACTIVE = 1;
    public static final int STATUS_INACTIVE = 0;
    public static final int STATUS_LOCKED = 2;
    
    /**
     * 密码最小长度
     */
    public static final int PASSWORD_MIN_LENGTH = 6;
    
    /**
     * 密码最大长度
     */
    public static final int PASSWORD_MAX_LENGTH = 20;
    
    /**
     * 用户名最小长度
     */
    public static final int USERNAME_MIN_LENGTH = 2;
    
    /**
     * 用户名最大长度
     */
    public static final int USERNAME_MAX_LENGTH = 20;
    
    private UserConstants() {
        // 工具类，禁止实例化
    }
}

