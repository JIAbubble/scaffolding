package com.example.performanceevaluation.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 定时任务配置
 * 通过 schedule.enabled 配置控制是否启用定时任务
 */
@Configuration
@EnableScheduling
@ConditionalOnProperty(name = "schedule.enabled", havingValue = "true", matchIfMissing = false)
public class ScheduleConfig {
}

