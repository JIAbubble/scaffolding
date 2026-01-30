package com.example.performanceevaluation.utils;

import com.example.performanceevaluation.constants.SystemConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 */
@Slf4j
public class JwtUtil {
    
    /**
     * JWT密钥（生产环境应该从配置文件读取）
     */
    private static final String SECRET = "performanceEvaluationSecretKeyForJWTTokenGeneration2024";
    
    /**
     * 生成Token
     * 
     * @param userId 用户ID
     * @param username 用户名
     * @return Token字符串
     */
    public static String generateToken(Long userId, String username) {
        return generateToken(userId, username, SystemConstants.TOKEN_EXPIRE_TIME);
    }
    
    /**
     * 生成Token
     * 
     * @param userId 用户ID
     * @param username 用户名
     * @param expireTime 过期时间（秒）
     * @return Token字符串
     */
    public static String generateToken(Long userId, String username, long expireTime) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expireTime * 1000);
        
        SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
        
        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key)
                .compact();
    }
    
    /**
     * 从Token中获取Claims
     * 
     * @param token Token字符串
     * @return Claims对象
     */
    public static Claims getClaimsFromToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            log.error("解析Token失败: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * 从Token中获取用户ID
     * 
     * @param token Token字符串
     * @return 用户ID
     */
    public static Long getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims == null) {
            return null;
        }
        Object userId = claims.get("userId");
        if (userId instanceof Integer) {
            return ((Integer) userId).longValue();
        }
        return (Long) userId;
    }
    
    /**
     * 从Token中获取用户名
     * 
     * @param token Token字符串
     * @return 用户名
     */
    public static String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims != null ? claims.getSubject() : null;
    }
    
    /**
     * 验证Token是否有效
     * 
     * @param token Token字符串
     * @return 是否有效
     */
    public static boolean validateToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            if (claims == null) {
                return false;
            }
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 获取Token过期时间
     * 
     * @param token Token字符串
     * @return 过期时间
     */
    public static Date getExpirationDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims != null ? claims.getExpiration() : null;
    }
}

