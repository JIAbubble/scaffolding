package com.example.performanceevaluation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 需要角色注解
 * 标记在Controller或方法上，表示需要特定角色才能访问
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresRole {
    /**
     * 需要的角色
     */
    String[] value();
}

