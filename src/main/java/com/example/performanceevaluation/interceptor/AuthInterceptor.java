package com.example.performanceevaluation.interceptor;

import com.example.performanceevaluation.annotation.RequiresAuth;
import com.example.performanceevaluation.annotation.RequiresRole;
import com.example.performanceevaluation.exception.BusinessException;
import com.example.performanceevaluation.pojo.ResultCode;
import com.example.performanceevaluation.pojo.User;
import com.example.performanceevaluation.service.UserService;
import com.example.performanceevaluation.utils.JwtUtil;
import com.example.performanceevaluation.utils.RedisUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;

/**
 * 认证拦截器
 * 支持通过配置开关控制是否启用认证
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {
    
    private final RedisUtil redisUtil;
    private final UserService userService;
    
    /**
     * 是否启用认证（默认关闭，通过配置文件控制）
     */
    @Value("${auth.enabled:false}")
    private boolean authEnabled;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 如果认证功能未启用，直接放行
        if (!authEnabled) {
            return true;
        }
        
        // 如果是OPTIONS请求，直接放行
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }
        
        // 检查是否有@RequiresAuth注解
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            RequiresAuth requiresAuth = handlerMethod.getMethodAnnotation(RequiresAuth.class);
            if (requiresAuth == null) {
                requiresAuth = handlerMethod.getBeanType().getAnnotation(RequiresAuth.class);
            }
            
            // 如果没有@RequiresAuth注解，直接放行
            if (requiresAuth == null) {
                return true;
            }
        }
        
        // 获取Token
        String token = getTokenFromRequest(request);
        
        if (!StringUtils.hasText(token)) {
            throw new BusinessException(ResultCode.UNAUTHORIZED, "未登录，请先登录");
        }
        
        // 验证Token
        if (!JwtUtil.validateToken(token)) {
            throw new BusinessException(ResultCode.UNAUTHORIZED, "Token已过期，请重新登录");
        }
        
        // 从Token中获取用户信息
        Long userId = JwtUtil.getUserIdFromToken(token);
        String username = JwtUtil.getUsernameFromToken(token);
        
        if (userId == null || !StringUtils.hasText(username)) {
            throw new BusinessException(ResultCode.TOKEN_INVALID, "Token无效");
        }
        
        // 验证Redis中的Token是否存在
        String redisToken = redisUtil.getToken(userId);
        if (!token.equals(redisToken)) {
            throw new BusinessException(ResultCode.UNAUTHORIZED, "Token无效，请重新登录");
        }
        
        // 将用户信息存储到request中，供后续使用
        request.setAttribute("userId", userId);
        request.setAttribute("username", username);
        
        // 检查角色权限
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            RequiresRole requiresRole = handlerMethod.getMethodAnnotation(RequiresRole.class);
            if (requiresRole == null) {
                requiresRole = handlerMethod.getBeanType().getAnnotation(RequiresRole.class);
            }
            
            if (requiresRole != null) {
                String[] requiredRoles = requiresRole.value();
                User user = userService.getById(userId);
                if (user == null || user.getRole() == null) {
                    throw new BusinessException(ResultCode.FORBIDDEN, "用户角色信息不存在");
                }
                
                boolean hasRole = Arrays.asList(requiredRoles).contains(user.getRole());
                if (!hasRole) {
                    throw new BusinessException(ResultCode.FORBIDDEN, "权限不足，需要角色: " + Arrays.toString(requiredRoles));
                }
            }
        }
        
        return true;
    }
    
    /**
     * 从请求中获取Token
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        // 1. 从Header中获取
        String token = request.getHeader("Authorization");
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        
        // 2. 从参数中获取
        token = request.getParameter("token");
        if (StringUtils.hasText(token)) {
            return token;
        }
        
        return null;
    }
}

