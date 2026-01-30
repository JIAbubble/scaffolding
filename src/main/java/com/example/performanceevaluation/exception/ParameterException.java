package com.example.performanceevaluation.exception;

import com.example.performanceevaluation.pojo.ResultCode;
import lombok.Getter;

import java.util.List;

/**
 * 参数异常类
 */
@Getter
public class ParameterException extends RuntimeException {
    
    private final Integer code;
    private final String message;
    private final List<String> errors;
    
    public ParameterException(String message) {
        super(message);
        this.code = ResultCode.PARAM_VALIDATE_ERROR.getCode();
        this.message = message;
        this.errors = null;
    }
    
    public ParameterException(String message, List<String> errors) {
        super(message);
        this.code = ResultCode.PARAM_VALIDATE_ERROR.getCode();
        this.message = message;
        this.errors = errors;
    }
    
    public ParameterException(List<String> errors) {
        super("参数校验失败");
        this.code = ResultCode.PARAM_VALIDATE_ERROR.getCode();
        this.message = "参数校验失败";
        this.errors = errors;
    }
}

