package com.example.performanceevaluation.controller;

import com.example.performanceevaluation.annotation.RequiresAuth;
import com.example.performanceevaluation.constants.SystemConstants;
import com.example.performanceevaluation.form.LoginForm;
import com.example.performanceevaluation.form.RegisterForm;
import com.example.performanceevaluation.pojo.LoginResult;
import com.example.performanceevaluation.pojo.User;
import com.example.performanceevaluation.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 认证Controller
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    
    /**
     * 用户注册
     */
    @PostMapping("/register")
    public User register(@Valid @RequestBody RegisterForm form) {
        return authService.register(form.getUsername(), form.getPassword(), form.getEmail());
    }
    
    /**
     * 用户登录
     */
    @PostMapping("/login")
    public LoginResult login(@Valid @RequestBody LoginForm form) {
        return authService.login(form.getUsername(), form.getPassword());
    }
    
    /**
     * 用户登出（需要登录）
     */
    @PostMapping("/logout")
    @RequiresAuth
    public void logout(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        authService.logout(userId);
    }
    
    /**
     * 刷新Token（需要登录）
     */
    @PostMapping("/refresh")
    @RequiresAuth
    public LoginResult refreshToken(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        String newToken = authService.refreshToken(userId);
        
        User user = authService.getUserById(userId);
        return LoginResult.builder()
                .token(newToken)
                .user(user)
                .expireTime(SystemConstants.TOKEN_EXPIRE_TIME)
                .build();
    }
}

