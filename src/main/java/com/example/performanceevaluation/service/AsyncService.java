package com.example.performanceevaluation.service;

/**
 * 异步任务服务示例
 */
public interface AsyncService {
    
    /**
     * 异步发送邮件
     */
    void sendEmailAsync(String to, String subject, String content);
    
    /**
     * 异步处理数据
     */
    void processDataAsync(String data);
}

