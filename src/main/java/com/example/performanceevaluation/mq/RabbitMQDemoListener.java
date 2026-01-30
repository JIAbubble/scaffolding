package com.example.performanceevaluation.mq;

import com.example.performanceevaluation.config.RabbitMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * RabbitMQ 消息监听示例
 */
@Slf4j
@Component
public class RabbitMQDemoListener {

    /**
     * 监听示例队列，消费消息
     */
    @RabbitListener(queues = RabbitMQConfig.DEMO_QUEUE)
    public void onMessage(Object message) {
        log.info("收到 RabbitMQ 消息: {}", message);
    }
}


