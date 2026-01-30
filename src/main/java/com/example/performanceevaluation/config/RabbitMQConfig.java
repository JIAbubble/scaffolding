package com.example.performanceevaluation.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 基础配置
 */
@Configuration
public class RabbitMQConfig {

    /**
     * 示例队列名称
     */
    public static final String DEMO_QUEUE = "demo.queue";

    /**
     * 示例交换机名称
     */
    public static final String DEMO_EXCHANGE = "demo.exchange";

    /**
     * 示例路由键
     */
    public static final String DEMO_ROUTING_KEY = "demo.routing.key";

    @Bean
    public Queue demoQueue() {
        // durable=true 表示队列持久化
        return new Queue(DEMO_QUEUE, true);
    }

    @Bean
    public DirectExchange demoExchange() {
        return new DirectExchange(DEMO_EXCHANGE, true, false);
    }

    @Bean
    public Binding demoBinding(Queue demoQueue, DirectExchange demoExchange) {
        return BindingBuilder.bind(demoQueue).to(demoExchange).with(DEMO_ROUTING_KEY);
    }

    /**
     * 使用 JSON 序列化消息
     */
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }
}


