package com.example.performanceevaluation.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 定时任务示例
 * 通过 schedule.enabled 配置控制是否启用定时任务
 * 当 schedule.enabled=false 时，整个类不会被加载
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "schedule.enabled", havingValue = "true", matchIfMissing = false)
public class ScheduledTask {
    
    /**
     * 每5秒执行一次
     * cron表达式：秒 分 时 日 月 周
     */
    @Scheduled(cron = "0/5 * * * * ?")
    public void task1() {
        log.info("定时任务1执行: {}", LocalDateTime.now());
    }
    
    /**
     * 每分钟执行一次
     */
    @Scheduled(cron = "0 * * * * ?")
    public void task2() {
        log.info("定时任务2执行: {}", LocalDateTime.now());
    }
    
    /**
     * 每天凌晨1点执行
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void task3() {
        log.info("定时任务3执行（每天凌晨1点）: {}", LocalDateTime.now());
    }
    
    /**
     * 固定延迟执行（上次执行完成后5秒再执行）
     */
    @Scheduled(fixedDelay = 5000)
    public void task4() {
        log.info("定时任务4执行（固定延迟）: {}", LocalDateTime.now());
    }
    
    /**
     * 固定频率执行（每10秒执行一次）
     */
    @Scheduled(fixedRate = 10000)
    public void task5() {
        log.info("定时任务5执行（固定频率）: {}", LocalDateTime.now());
    }
}

