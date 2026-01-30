package com.example.performanceevaluation.controller;

import com.example.performanceevaluation.service.AsyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 异步任务示例 Controller
 */
@RestController
@RequestMapping("/api/async")
@RequiredArgsConstructor
public class AsyncDemoController {

    private final AsyncService asyncService;

    /**
     * 触发异步发送邮件示例
     */
    @GetMapping("/sendEmail")
    public String sendEmail(@RequestParam String to,
                            @RequestParam String subject,
                            @RequestParam String content) {
        asyncService.sendEmailAsync(to, subject, content);
        return "邮件已提交异步发送";
    }

    /**
     * 触发异步数据处理示例
     */
    @GetMapping("/processData")
    public String processData(@RequestParam String data) {
        asyncService.processDataAsync(data);
        return "数据处理任务已提交";
    }
}


