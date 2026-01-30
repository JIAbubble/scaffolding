package com.example.performanceevaluation.constants;

/**
 * 系统常量
 */
public class SystemConstants {
    
    /**
     * 默认分页大小
     */
    public static final int DEFAULT_PAGE_SIZE = 10;
    
    /**
     * 最大分页大小
     */
    public static final int MAX_PAGE_SIZE = 100;
    
    /**
     * 默认页码
     */
    public static final int DEFAULT_PAGE_NUM = 1;
    
    /**
     * Token过期时间（秒）- 7天
     */
    public static final long TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60;
    
    /**
     * Token刷新时间（秒）- 1天
     */
    public static final long TOKEN_REFRESH_TIME = 24 * 60 * 60;
    
    /**
     * Redis Key前缀
     */
    public static final String REDIS_KEY_PREFIX = "performance:evaluation:";
    
    /**
     * Token存储Key前缀
     */
    public static final String REDIS_TOKEN_KEY_PREFIX = REDIS_KEY_PREFIX + "token:";
    
    /**
     * 用户信息存储Key前缀
     */
    public static final String REDIS_USER_KEY_PREFIX = REDIS_KEY_PREFIX + "user:";
    
    /**
     * 文件上传最大大小（字节）- 100MB
     */
    public static final long MAX_FILE_SIZE = 100 * 1024 * 1024;
    
    /**
     * 允许的文件扩展名
     */
    public static final String[] ALLOWED_FILE_EXTENSIONS = {
        "jpg", "jpeg", "png", "gif", "pdf", "doc", "docx", "xls", "xlsx"
    };
    
    private SystemConstants() {
        // 工具类，禁止实例化
    }
}

