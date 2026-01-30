package com.example.performanceevaluation.pojo;

import lombok.Data;
import lombok.Getter;

@Getter
public enum ResultCode {
    // 成功状态
    SUCCESS(200, "操作成功"),

    // 客户端错误
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未授权访问"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),

    // 服务器错误
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    SERVICE_UNAVAILABLE(503, "服务不可用"),

    // 业务错误（可扩展）
    PARAM_VALIDATE_ERROR(1001, "参数校验失败"),
    USER_NOT_LOGIN(1002, "用户未登录"),
    DATA_NOT_EXIST(1003, "数据不存在"),
    DATA_EXIST(1004, "数据已存在"),
    
    // 认证相关错误
    TOKEN_INVALID(1005, "Token无效"),
    TOKEN_EXPIRED(1006, "Token已过期"),
    LOGIN_FAILED(1007, "登录失败"),
    REGISTER_FAILED(1008, "注册失败");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}