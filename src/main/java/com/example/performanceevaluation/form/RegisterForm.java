package com.example.performanceevaluation.form;

import com.example.performanceevaluation.constants.UserConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 注册表单
 */
@Data
public class RegisterForm {
    
    @NotBlank(message = "用户名不能为空")
    @Size(min = UserConstants.USERNAME_MIN_LENGTH, max = UserConstants.USERNAME_MAX_LENGTH, 
          message = "用户名长度必须在3-20之间")
    private String username;
    
    @NotBlank(message = "密码不能为空")
    @Size(min = UserConstants.PASSWORD_MIN_LENGTH, max = UserConstants.PASSWORD_MAX_LENGTH,
          message = "密码长度必须在6-20之间")
    private String password;
    
    @Email(message = "邮箱格式不正确")
    private String email;
}

