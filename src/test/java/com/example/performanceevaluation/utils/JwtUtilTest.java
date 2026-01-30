package com.example.performanceevaluation.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JwtUtil测试示例
 */
class JwtUtilTest {
    
    @Test
    void testGenerateAndValidateToken() {
        Long userId = 1L;
        String username = "testuser";
        
        // 生成Token
        String token = JwtUtil.generateToken(userId, username);
        assertNotNull(token);
        
        // 验证Token
        assertTrue(JwtUtil.validateToken(token));
        
        // 从Token中获取信息
        assertEquals(userId, JwtUtil.getUserIdFromToken(token));
        assertEquals(username, JwtUtil.getUsernameFromToken(token));
    }
    
    @Test
    void testInvalidToken() {
        assertFalse(JwtUtil.validateToken("invalid.token.here"));
        assertNull(JwtUtil.getUserIdFromToken("invalid.token.here"));
    }
}

