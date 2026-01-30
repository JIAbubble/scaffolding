package com.example.performanceevaluation.service.impl;

import com.example.performanceevaluation.config.RabbitMQConfig;
import com.example.performanceevaluation.service.RabbitMQService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * RabbitMQ 消息队列示例服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitMQServiceImpl implements RabbitMQService {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void sendTextMessage(String message) {
        log.info("发送文本消息到 RabbitMQ: {}", message);
        rabbitTemplate.convertAndSend(RabbitMQConfig.DEMO_EXCHANGE,
                RabbitMQConfig.DEMO_ROUTING_KEY, message);
    }

    @Override
    public void sendBusinessMessage(Object payload) {
        Map<String, Object> msg = new HashMap<>();
        msg.put("payload", payload);
        msg.put("timestamp", System.currentTimeMillis());
        log.info("发送业务消息到 RabbitMQ: {}", msg);
        rabbitTemplate.convertAndSend(RabbitMQConfig.DEMO_EXCHANGE,
                RabbitMQConfig.DEMO_ROUTING_KEY, msg);
    }
}


