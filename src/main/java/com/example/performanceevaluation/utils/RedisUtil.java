package com.example.performanceevaluation.utils;

import com.example.performanceevaluation.constants.SystemConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RedisUtil {
    
    private final RedisTemplate<String, Object> redisTemplate;
    
    /**
     * 设置缓存
     * 
     * @param key 键
     * @param value 值
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(SystemConstants.REDIS_KEY_PREFIX + key, value);
    }
    
    /**
     * 设置缓存（带过期时间）
     * 
     * @param key 键
     * @param value 值
     * @param timeout 过期时间（秒）
     */
    public void set(String key, Object value, long timeout) {
        redisTemplate.opsForValue().set(SystemConstants.REDIS_KEY_PREFIX + key, value, timeout, TimeUnit.SECONDS);
    }
    
    /**
     * 获取缓存
     * 
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(SystemConstants.REDIS_KEY_PREFIX + key);
    }
    
    /**
     * 获取缓存（指定类型）
     * 
     * @param key 键
     * @param clazz 类型
     * @return 值
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> clazz) {
        Object value = get(key);
        return value != null ? (T) value : null;
    }
    
    /**
     * 删除缓存
     * 
     * @param key 键
     * @return 是否成功
     */
    public Boolean delete(String key) {
        return redisTemplate.delete(SystemConstants.REDIS_KEY_PREFIX + key);
    }
    
    /**
     * 判断key是否存在
     * 
     * @param key 键
     * @return 是否存在
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(SystemConstants.REDIS_KEY_PREFIX + key);
    }
    
    /**
     * 设置过期时间
     * 
     * @param key 键
     * @param timeout 过期时间（秒）
     * @return 是否成功
     */
    public Boolean expire(String key, long timeout) {
        return redisTemplate.expire(SystemConstants.REDIS_KEY_PREFIX + key, timeout, TimeUnit.SECONDS);
    }
    
    /**
     * 获取过期时间
     * 
     * @param key 键
     * @return 过期时间（秒）
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(SystemConstants.REDIS_KEY_PREFIX + key, TimeUnit.SECONDS);
    }
    
    /**
     * 递增
     * 
     * @param key 键
     * @param delta 增量
     * @return 递增后的值
     */
    public Long increment(String key, long delta) {
        return redisTemplate.opsForValue().increment(SystemConstants.REDIS_KEY_PREFIX + key, delta);
    }
    
    /**
     * 递减
     * 
     * @param key 键
     * @param delta 减量
     * @return 递减后的值
     */
    public Long decrement(String key, long delta) {
        return redisTemplate.opsForValue().decrement(SystemConstants.REDIS_KEY_PREFIX + key, delta);
    }
    
    /**
     * 获取所有匹配的key
     * 
     * @param pattern 匹配模式
     * @return key集合
     */
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(SystemConstants.REDIS_KEY_PREFIX + pattern);
    }
    
    /**
     * 存储Token
     * 
     * @param userId 用户ID
     * @param token Token
     */
    public void setToken(Long userId, String token) {
        String key = SystemConstants.REDIS_TOKEN_KEY_PREFIX + userId;
        set(key.replace(SystemConstants.REDIS_KEY_PREFIX, ""), token, SystemConstants.TOKEN_EXPIRE_TIME);
    }
    
    /**
     * 获取Token
     * 
     * @param userId 用户ID
     * @return Token
     */
    public String getToken(Long userId) {
        String key = SystemConstants.REDIS_TOKEN_KEY_PREFIX + userId;
        return get(key.replace(SystemConstants.REDIS_KEY_PREFIX, ""), String.class);
    }
    
    /**
     * 删除Token
     * 
     * @param userId 用户ID
     */
    public void deleteToken(Long userId) {
        String key = SystemConstants.REDIS_TOKEN_KEY_PREFIX + userId;
        delete(key.replace(SystemConstants.REDIS_KEY_PREFIX, ""));
    }
}

