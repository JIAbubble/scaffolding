package com.example.performanceevaluation.service.impl;

import com.example.performanceevaluation.service.AsyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 异步任务服务实现示例
 */
@Slf4j
@Service
public class AsyncServiceImpl implements AsyncService {
    
    @Override
    @Async("taskExecutor")
    public void sendEmailAsync(String to, String subject, String content) {
        log.info("开始异步发送邮件到: {}", to);
        try {
            // 模拟发送邮件
            Thread.sleep(2000);
            log.info("邮件发送成功: {} - {}", to, subject);
        } catch (InterruptedException e) {
            log.error("发送邮件失败", e);
            Thread.currentThread().interrupt();
        }
    }
    
    @Override
    @Async("taskExecutor")
    public void processDataAsync(String data) {
        log.info("开始异步处理数据: {}", data);
        try {
            // 模拟数据处理
            Thread.sleep(3000);
            log.info("数据处理完成: {}", data);
        } catch (InterruptedException e) {
            log.error("处理数据失败", e);
            Thread.currentThread().interrupt();
        }
    }
}

