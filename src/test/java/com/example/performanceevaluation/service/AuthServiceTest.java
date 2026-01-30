package com.example.performanceevaluation.service;

import com.example.performanceevaluation.pojo.LoginResult;
import com.example.performanceevaluation.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * AuthService测试示例
 */
@SpringBootTest
@Transactional
@Rollback
class AuthServiceTest {
    
    @Autowired
    private AuthService authService;
    
    @Test
    void testRegister() {
        User user = authService.register("testuser", "Test123456", "test@example.com");
        assertNotNull(user);
        assertEquals("testuser", user.getUsername());
        assertNotNull(user.getPassword());

    }
    
    @Test
    void testLogin() {
        // 先注册
        authService.register("testuser", "Test123456", "test@example.com");
        
        // 再登录
        LoginResult result = authService.login("testuser", "Test123456");
        assertNotNull(result);
        assertNotNull(result.getToken());
        assertNotNull(result.getUser());
    }
}

