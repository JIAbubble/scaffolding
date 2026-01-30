package com.example.performanceevaluation.config;

import com.example.performanceevaluation.pojo.ResponseResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 统一响应包装器
 * 自动将Controller返回值包装为ResponseResult格式
 */
@Slf4j
@RestControllerAdvice(basePackages = "com.example.performanceevaluation.controller")
public class GlobalResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 判断是否支持该返回值类型
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 如果返回值已经是ResponseResult类型，则不包装
        return !returnType.getParameterType().equals(ResponseResult.class);
    }

    /**
     * 在响应写入之前进行处理
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                   Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                   ServerHttpRequest request, ServerHttpResponse response) {
        
        // 如果body已经是ResponseResult类型，直接返回
        if (body instanceof ResponseResult) {
            return body;
        }
        
        // 如果body为null，返回成功响应
        if (body == null) {
            return ResponseResult.success();
        }
        
        // 包装为ResponseResult
        ResponseResult<Object> result = ResponseResult.success(body);
        
        // 如果返回类型是String，需要特殊处理
        // 因为String类型会被StringHttpMessageConverter处理，需要转换为JSON字符串
        if (returnType.getParameterType().equals(String.class)) {
            try {
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                return objectMapper.writeValueAsString(result);
            } catch (JsonProcessingException e) {
                log.error("JSON序列化失败", e);
                return ResponseResult.fail("响应序列化失败");
            }
        }
        
        return result;
    }
}

