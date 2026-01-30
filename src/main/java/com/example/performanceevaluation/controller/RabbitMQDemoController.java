package com.example.performanceevaluation.controller;

import com.example.performanceevaluation.service.RabbitMQService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * RabbitMQ 消息队列示例 Controller
 *
 * 注意：返回值会被 GlobalResponseBodyAdvice 自动包装
 */
@RestController
@RequestMapping("/api/mq")
@RequiredArgsConstructor
public class RabbitMQDemoController {

    private final RabbitMQService rabbitMQService;

    /**
     * 发送简单文本消息
     */
    @PostMapping("/sendText")
    public String sendText(@RequestBody Map<String, String> body) {
        String message = body.getOrDefault("message", "hello rabbitmq");
        rabbitMQService.sendTextMessage(message);
        return "消息已发送: " + message;
    }

    /**
     * 发送业务对象消息
     */
    @PostMapping("/sendBusiness")
    public String sendBusiness(@RequestBody Map<String, Object> payload) {
        rabbitMQService.sendBusinessMessage(payload);
        return "业务消息已发送";
    }
}


