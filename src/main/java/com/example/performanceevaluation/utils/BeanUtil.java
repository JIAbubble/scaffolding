package com.example.performanceevaluation.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Bean工具类（封装Spring BeanUtils）
 */
public class BeanUtil {

    /**
     * 复制对象属性（浅拷贝）
     *
     * @param source 源对象
     * @param target 目标对象
     */
    public static void copyProperties(Object source, Object target) {
        if (source == null || target == null) {
            return;
        }
        BeanUtils.copyProperties(source, target);
    }

    /**
     * 复制对象属性（忽略指定属性）
     *
     * @param source           源对象
     * @param target           目标对象
     * @param ignoreProperties 忽略的属性名数组
     */
    public static void copyProperties(Object source, Object target, String... ignoreProperties) {
        if (source == null || target == null) {
            return;
        }
        BeanUtils.copyProperties(source, target, ignoreProperties);
    }

    /**
     * 复制对象属性（忽略null值）
     *
     * @param source 源对象
     * @param target 目标对象
     */
    public static void copyPropertiesIgnoreNull(Object source, Object target) {
        if (source == null || target == null) {
            return;
        }
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    /**
     * 对象转换（创建新对象）
     *
     * @param source 源对象
     * @param clazz  目标类型
     * @param <T>    目标类型
     * @return 转换后的对象
     */
    public static <T> T convert(Object source, Class<T> clazz) {
        if (source == null) {
            return null;
        }
        try {
            T target = clazz.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(source, target);
            return target;
        } catch (Exception e) {
            throw new RuntimeException("对象转换失败: " + e.getMessage(), e);
        }
    }

    /**
     * 对象转换（忽略null值）
     *
     * @param source 源对象
     * @param clazz  目标类型
     * @param <T>    目标类型
     * @return 转换后的对象
     */
    public static <T> T convertIgnoreNull(Object source, Class<T> clazz) {
        if (source == null) {
            return null;
        }
        try {
            T target = clazz.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
            return target;
        } catch (Exception e) {
            throw new RuntimeException("对象转换失败: " + e.getMessage(), e);
        }
    }

    /**
     * List对象转换
     *
     * @param sourceList 源对象列表
     * @param clazz      目标类型
     * @param <S>        源类型
     * @param <T>        目标类型
     * @return 转换后的对象列表
     */
    public static <S, T> List<T> convertList(List<S> sourceList, Class<T> clazz) {
        if (sourceList == null || sourceList.isEmpty()) {
            return List.of();
        }
        return sourceList.stream()
                .map(source -> convert(source, clazz))
                .collect(Collectors.toList());
    }

    /**
     * 获取对象中为null的属性名数组
     *
     * @param source 源对象
     * @return 属性名数组
     */
    private static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    /**
     * 判断对象是否为空（所有属性都为null）
     *
     * @param obj 对象
     * @return 是否为空
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        BeanWrapper wrapper = new BeanWrapperImpl(obj);
        java.beans.PropertyDescriptor[] pds = wrapper.getPropertyDescriptors();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object value = wrapper.getPropertyValue(pd.getName());
            if (value != null && !"class".equals(pd.getName())) {
                return false;
            }
        }
        return true;
    }
}

