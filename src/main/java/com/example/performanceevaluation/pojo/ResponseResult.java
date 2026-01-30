package com.example.performanceevaluation.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ResponseResult<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    // 状态码（0或200表示成功）
    private Integer code;
    // 成功状态（true/false）
    private Boolean status;
    // 提示消息
    private String message;
    // 返回数据
    private T data;

    // 私有构造方法，强制使用静态工厂方法
    private ResponseResult() {}

    // 核心创建方法
    private static <T> ResponseResult<T> create(Integer code, Boolean status, String message, T data) {
        ResponseResult<T> result = new ResponseResult<>();
        result.setCode(code);
        result.setStatus(status);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    // 成功响应方法
    public static <T> ResponseResult<T> success() {
        return create(ResultCode.SUCCESS.getCode(), true, "操作成功", null);
    }

    public static <T> ResponseResult<T> success(T data) {
        return create(ResultCode.SUCCESS.getCode(), true, "操作成功", data);
    }

    public static <T> ResponseResult<T> success(String message, T data) {
        return create(ResultCode.SUCCESS.getCode(), true, message, data);
    }

    // 失败响应方法
    public static <T> ResponseResult<T> fail(String message) {
        return create(ResultCode.BAD_REQUEST.getCode(), false, message, null);
    }

    public static <T> ResponseResult<T> fail(Integer code, String message) {
        return create(code, false, message, null);
    }

    public static <T> ResponseResult<T> fail(ResultCode resultCode) {
        return create(resultCode.getCode(), false, resultCode.getMessage(), null);
    }

    public static <T> ResponseResult<T> fail(ResultCode resultCode, String customMessage) {
        return create(resultCode.getCode(), false, customMessage, null);
    }

    // 便捷判断方法
    public boolean isSuccess() {
        return Boolean.TRUE.equals(status) &&
                (code == null || code.equals(ResultCode.SUCCESS.getCode()) || code == 200);
    }
}