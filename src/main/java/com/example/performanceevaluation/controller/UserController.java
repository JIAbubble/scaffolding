package com.example.performanceevaluation.controller;

import com.example.performanceevaluation.exception.BusinessException;
import com.example.performanceevaluation.form.UserCreateForm;
import com.example.performanceevaluation.pojo.ResultCode;
import com.example.performanceevaluation.pojo.User;
import com.example.performanceevaluation.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户Controller
 * 注意：返回值会自动被GlobalResponseBodyAdvice包装为ResponseResult格式
 * 异常会自动被GlobalExceptionHandler处理
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 获取用户列表
     * 返回值会自动包装为 ResponseResult<List<User>>
     */
    @GetMapping
    public List<User> list() {
        return userService.list();
    }

    /**
     * 获取用户详情
     * 如果用户不存在，会抛出BusinessException，全局异常处理器会自动处理
     */
    @GetMapping("/{id}")
    public User detail(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null) {
            throw new BusinessException(ResultCode.DATA_NOT_EXIST, "用户不存在");
        }
        return user;
    }

    /**
     * 创建用户
     * @Validated 注解的参数校验失败会自动被全局异常处理器处理
     */
    @PostMapping
    public User create(@Validated @RequestBody UserCreateForm form) {
        User user = User.builder()
                .username(form.getUsername())
                .email(form.getEmail())
                .role(form.getRole())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        userService.save(user);
        return user;
    }

    /**
     * 更新用户
     */
    @PutMapping("/{id}")
    public User update(@PathVariable Long id, @Validated @RequestBody UserCreateForm form) {
        User existing = userService.getById(id);
        if (existing == null) {
            throw new BusinessException(ResultCode.DATA_NOT_EXIST, "用户不存在");
        }
        existing.setUsername(form.getUsername());
        existing.setEmail(form.getEmail());
        existing.setRole(form.getRole());
        existing.setUpdatedAt(LocalDateTime.now());
        userService.updateById(existing);
        return existing;
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        if (!userService.removeById(id)) {
            throw new BusinessException(ResultCode.DATA_NOT_EXIST, "用户不存在");
        }
    }
}

