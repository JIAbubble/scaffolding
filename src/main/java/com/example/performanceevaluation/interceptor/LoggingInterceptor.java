package com.example.performanceevaluation.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

/**
 * 请求日志拦截器
 * 记录请求和响应信息，便于调试和监控
 */
@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private static final String START_TIME = "startTime";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 记录请求开始时间
        request.setAttribute(START_TIME, System.currentTimeMillis());
        
        // 记录请求信息
        if (log.isDebugEnabled()) {
            logRequest(request);
        }
        
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
                               Object handler, Exception ex) {
        // 计算请求耗时
        Long startTime = (Long) request.getAttribute(START_TIME);
        if (startTime != null) {
            long duration = System.currentTimeMillis() - startTime;
            
            // 记录响应信息
            if (log.isDebugEnabled()) {
                logResponse(request, response, duration, ex);
            } else {
                // INFO级别只记录关键信息
                log.info("请求完成 - {} {} - 状态码: {} - 耗时: {}ms", 
                        request.getMethod(), 
                        request.getRequestURI(),
                        response.getStatus(),
                        duration);
            }
        }
    }

    /**
     * 记录请求信息
     */
    private void logRequest(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n========== 请求开始 ==========\n");
        sb.append("请求方法: ").append(request.getMethod()).append("\n");
        sb.append("请求URI: ").append(request.getRequestURI()).append("\n");
        sb.append("请求URL: ").append(request.getRequestURL()).append("\n");
        sb.append("客户端IP: ").append(getClientIp(request)).append("\n");
        sb.append("请求协议: ").append(request.getProtocol()).append("\n");
        
        // 请求头
        sb.append("请求头:\n");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            sb.append("  ").append(headerName).append(": ").append(request.getHeader(headerName)).append("\n");
        }
        
        // 请求参数
        sb.append("请求参数:\n");
        Enumeration<String> parameterNames = request.getParameterNames();
        if (parameterNames.hasMoreElements()) {
            while (parameterNames.hasMoreElements()) {
                String paramName = parameterNames.nextElement();
                sb.append("  ").append(paramName).append(": ").append(request.getParameter(paramName)).append("\n");
            }
        } else {
            sb.append("  (无)\n");
        }
        
        // 请求体（如果是POST/PUT请求）
        if (request instanceof ContentCachingRequestWrapper) {
            ContentCachingRequestWrapper wrapper = (ContentCachingRequestWrapper) request;
            byte[] content = wrapper.getContentAsByteArray();
            if (content.length > 0) {
                String body = new String(content, StandardCharsets.UTF_8);
                sb.append("请求体: ").append(body).append("\n");
            }
        }
        
        sb.append("==============================");
        log.debug(sb.toString());
    }

    /**
     * 记录响应信息
     */
    private void logResponse(HttpServletRequest request, HttpServletResponse response, 
                            long duration, Exception ex) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n========== 请求结束 ==========\n");
        sb.append("请求方法: ").append(request.getMethod()).append("\n");
        sb.append("请求URI: ").append(request.getRequestURI()).append("\n");
        sb.append("响应状态码: ").append(response.getStatus()).append("\n");
        sb.append("耗时: ").append(duration).append("ms\n");
        
        // 响应头
        sb.append("响应头:\n");
        response.getHeaderNames().forEach(headerName -> {
            sb.append("  ").append(headerName).append(": ").append(response.getHeader(headerName)).append("\n");
        });
        
        // 响应体
        if (response instanceof ContentCachingResponseWrapper) {
            ContentCachingResponseWrapper wrapper = (ContentCachingResponseWrapper) response;
            byte[] content = wrapper.getContentAsByteArray();
            if (content.length > 0) {
                String body = new String(content, StandardCharsets.UTF_8);
                // 限制响应体长度，避免日志过大
                if (body.length() > 1000) {
                    body = body.substring(0, 1000) + "...(已截断)";
                }
                sb.append("响应体: ").append(body).append("\n");
            }
        }
        
        if (ex != null) {
            sb.append("异常: ").append(ex.getMessage()).append("\n");
        }
        
        sb.append("==============================");
        log.debug(sb.toString());
    }

    /**
     * 获取客户端真实IP
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 处理多个IP的情况
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}

