package com.example.performanceevaluation.exception;

import com.example.performanceevaluation.pojo.ResponseResult;
import com.example.performanceevaluation.pojo.ResultCode;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResponseResult<Object> handleBusinessException(BusinessException e) {
        log.warn("业务异常: {}", e.getMessage());
        return ResponseResult.fail(e.getCode(), e.getMessage());
    }

    /**
     * 处理参数异常
     */
    @ExceptionHandler(ParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult<Object> handleParameterException(ParameterException e) {
        log.warn("参数异常: {}", e.getMessage());
        ResponseResult<Object> result = ResponseResult.fail(e.getCode(), e.getMessage());
        if (e.getErrors() != null && !e.getErrors().isEmpty()) {
            result.setData(e.getErrors());
        }
        return result;
    }

    /**
     * 处理参数校验异常（@Validated 注解）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn("参数校验异常: {}", e.getMessage());
        List<String> errors = new ArrayList<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            if (error instanceof FieldError) {
                FieldError fieldError = (FieldError) error;
                errors.add(fieldError.getField() + ": " + fieldError.getDefaultMessage());
            } else {
                errors.add(error.getDefaultMessage());
            }
        });
        return ResponseResult.fail(ResultCode.PARAM_VALIDATE_ERROR.getCode(), 
                "参数校验失败: " + String.join(", ", errors));
    }

    /**
     * 处理参数绑定异常（@ModelAttribute 注解）
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult<Object> handleBindException(BindException e) {
        log.warn("参数绑定异常: {}", e.getMessage());
        List<String> errors = new ArrayList<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            if (error instanceof FieldError) {
                FieldError fieldError = (FieldError) error;
                errors.add(fieldError.getField() + ": " + fieldError.getDefaultMessage());
            } else {
                errors.add(error.getDefaultMessage());
            }
        });
        return ResponseResult.fail(ResultCode.PARAM_VALIDATE_ERROR.getCode(), 
                "参数绑定失败: " + String.join(", ", errors));
    }

    /**
     * 处理参数类型不匹配异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.warn("参数类型不匹配: {}", e.getMessage());
        String typeName = "未知";
        if (e.getRequiredType() != null) {
            typeName = e.getRequiredType().getSimpleName();
        }
        String message = String.format("参数 '%s' 类型不匹配，期望类型: %s", e.getName(), typeName);
        return ResponseResult.fail(ResultCode.PARAM_VALIDATE_ERROR.getCode(), message);
    }

    /**
     * 处理约束违反异常（@Valid 注解）
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult<Object> handleConstraintViolationException(ConstraintViolationException e) {
        log.warn("约束违反异常: {}", e.getMessage());
        List<String> errors = new ArrayList<>();
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        violations.forEach(violation -> {
            errors.add(violation.getPropertyPath() + ": " + violation.getMessage());
        });
        return ResponseResult.fail(ResultCode.PARAM_VALIDATE_ERROR.getCode(), 
                "参数校验失败: " + String.join(", ", errors));
    }

    /**
     * 处理空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseResult<Object> handleNullPointerException(NullPointerException e) {
        log.error("空指针异常", e);
        return ResponseResult.fail(ResultCode.INTERNAL_SERVER_ERROR);
    }

    /**
     * 处理非法参数异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult<Object> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("非法参数异常: {}", e.getMessage());
        return ResponseResult.fail(ResultCode.BAD_REQUEST.getCode(), e.getMessage());
    }

    /**
     * 处理其他未捕获的异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseResult<Object> handleException(Exception e) {
        log.error("系统异常", e);
        return ResponseResult.fail(ResultCode.INTERNAL_SERVER_ERROR.getCode(), 
                "系统异常，请联系管理员");
    }
}

