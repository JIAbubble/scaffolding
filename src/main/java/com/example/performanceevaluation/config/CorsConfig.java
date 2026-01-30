package com.example.performanceevaluation.config;

import com.example.performanceevaluation.interceptor.AuthInterceptor;
import com.example.performanceevaluation.interceptor.LoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC配置
 * 包括CORS配置和拦截器注册
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Autowired
    private LoggingInterceptor loggingInterceptor;
    
    @Autowired(required = false)
    private AuthInterceptor authInterceptor;
    
    /**
     * 是否启用认证（默认关闭）
     */
    @Value("${auth.enabled:false}")
    private boolean authEnabled;

    /**
     * CORS跨域配置
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(false)
                .maxAge(3600);
    }

    /**
     * 注册拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册日志拦截器
        registry.addInterceptor(loggingInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/swagger-ui.html",
                        "/error",
                        "/static/favicon.ico"
                );
        
        // 注册认证拦截器（如果启用认证功能）
        if (authEnabled && authInterceptor != null) {
            registry.addInterceptor(authInterceptor)
                    .addPathPatterns("/**")
                    .excludePathPatterns(
                            "/api/auth/login",
                            "/api/auth/register",
                            "/swagger-ui/**",
                            "/v3/api-docs/**",
                            "/swagger-ui.html",
                            "/error",
                            "/static/favicon.ico"
                    );
        }
    }
}

