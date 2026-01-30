package com.example.performanceevaluation.service;

/**
 * RabbitMQ 消息队列示例服务
 */
public interface RabbitMQService {

    /**
     * 发送简单文本消息
     */
    void sendTextMessage(String message);

    /**
     * 发送带业务数据的消息示例
     */
    void sendBusinessMessage(Object payload);
}


