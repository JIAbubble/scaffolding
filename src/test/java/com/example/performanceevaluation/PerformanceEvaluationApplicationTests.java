package com.example.performanceevaluation;

import com.example.performanceevaluation.utils.SecurityUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PerformanceEvaluationApplicationTests {

    @Test
    void contextLoads() {
        System.out.println(SecurityUtil.md5("123456"));
    }

        private static final Logger logger = LoggerFactory.getLogger(PerformanceEvaluationApplicationTests.class);

        public static void main(String[] args) {
            logger.info("SLF4J 测试信息");
            System.out.println("如果这行能打印且上一行没报错，说明SLF4J配置成功。");
             // 生成盐值
             String salt = SecurityUtil.generateRandomString(16);
            System.out.println(SecurityUtil.hashPassword("123456",salt));
            System.out.println(SecurityUtil.verifyPassword("12345678", salt, SecurityUtil.hashPassword("123456",salt)));
        }
}
