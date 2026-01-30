package com.example.performanceevaluation;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com.example.performanceevaluation.dao"})
public class PerformanceEvaluationApplication {

    public static void main(String[] args) {
        SpringApplication.run(PerformanceEvaluationApplication.class, args);
    }

}
