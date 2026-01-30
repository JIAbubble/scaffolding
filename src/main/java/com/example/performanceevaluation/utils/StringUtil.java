package com.example.performanceevaluation.utils;

import java.util.Collection;

/**
 * 字符串工具类
 */
public class StringUtil {

    /**
     * 判断字符串是否为空（null、空字符串、只包含空白字符）
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * 判断字符串是否不为空
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * 判断字符串是否为空（null 或空字符串）
     */
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * 判断字符串是否不为空
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 如果字符串为空，返回默认值
     */
    public static String defaultIfBlank(String str, String defaultValue) {
        return isBlank(str) ? defaultValue : str;
    }

    /**
     * 如果字符串为null，返回空字符串
     */
    public static String nullToEmpty(String str) {
        return str == null ? "" : str;
    }

    /**
     * 去除首尾空白字符
     */
    public static String trim(String str) {
        return str == null ? null : str.trim();
    }

    /**
     * 去除所有空白字符
     */
    public static String removeWhitespace(String str) {
        return str == null ? null : str.replaceAll("\\s+", "");
    }

    /**
     * 首字母大写
     */
    public static String capitalize(String str) {
        if (isBlank(str)) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * 首字母小写
     */
    public static String uncapitalize(String str) {
        if (isBlank(str)) {
            return str;
        }
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    /**
     * 驼峰命名转下划线命名
     */
    public static String camelToUnderscore(String str) {
        if (isBlank(str)) {
            return str;
        }
        return str.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }

    /**
     * 下划线命名转驼峰命名
     */
    public static String underscoreToCamel(String str) {
        if (isBlank(str)) {
            return str;
        }
        String[] parts = str.split("_");
        StringBuilder result = new StringBuilder(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            result.append(capitalize(parts[i]));
        }
        return result.toString();
    }

    /**
     * 截取字符串（安全截取，不会越界）
     */
    public static String substring(String str, int start, int end) {
        if (str == null) {
            return null;
        }
        int len = str.length();
        if (start < 0) {
            start = 0;
        }
        if (end > len) {
            end = len;
        }
        if (start > end) {
            return "";
        }
        return str.substring(start, end);
    }

    /**
     * 截取字符串（从指定位置开始）
     */
    public static String substring(String str, int start) {
        if (str == null) {
            return null;
        }
        if (start < 0) {
            start = 0;
        }
        if (start >= str.length()) {
            return "";
        }
        return str.substring(start);
    }

    /**
     * 字符串左填充
     */
    public static String leftPad(String str, int size, char padChar) {
        if (str == null) {
            return null;
        }
        int pads = size - str.length();
        if (pads <= 0) {
            return str;
        }
        return repeat(padChar, pads) + str;
    }

    /**
     * 字符串右填充
     */
    public static String rightPad(String str, int size, char padChar) {
        if (str == null) {
            return null;
        }
        int pads = size - str.length();
        if (pads <= 0) {
            return str;
        }
        return str + repeat(padChar, pads);
    }

    /**
     * 重复字符
     */
    public static String repeat(char ch, int count) {
        if (count <= 0) {
            return "";
        }
        return String.valueOf(ch).repeat(count);
    }

    /**
     * 字符串连接（使用指定分隔符）
     */
    public static String join(Collection<?> collection, String separator) {
        if (collection == null || collection.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Object item : collection) {
            if (!first) {
                sb.append(separator);
            }
            sb.append(item);
            first = false;
        }
        return sb.toString();
    }

    /**
     * 字符串连接（使用指定分隔符）
     */
    public static String join(Object[] array, String separator) {
        if (array == null || array.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                sb.append(separator);
            }
            sb.append(array[i]);
        }
        return sb.toString();
    }

    /**
     * 判断字符串是否包含指定子串（忽略大小写）
     */
    public static boolean containsIgnoreCase(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        return str.toLowerCase().contains(searchStr.toLowerCase());
    }

    /**
     * 判断字符串是否以指定前缀开头（忽略大小写）
     */
    public static boolean startsWithIgnoreCase(String str, String prefix) {
        if (str == null || prefix == null) {
            return false;
        }
        return str.toLowerCase().startsWith(prefix.toLowerCase());
    }

    /**
     * 判断字符串是否以指定后缀结尾（忽略大小写）
     */
    public static boolean endsWithIgnoreCase(String str, String suffix) {
        if (str == null || suffix == null) {
            return false;
        }
        return str.toLowerCase().endsWith(suffix.toLowerCase());
    }

    /**
     * 隐藏手机号中间4位
     */
    public static String maskPhone(String phone) {
        if (isBlank(phone) || phone.length() < 11) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }

    /**
     * 隐藏邮箱中间部分
     */
    public static String maskEmail(String email) {
        if (isBlank(email) || !email.contains("@")) {
            return email;
        }
        int atIndex = email.indexOf("@");
        String prefix = email.substring(0, Math.min(2, atIndex));
        String suffix = email.substring(atIndex);
        return prefix + "***" + suffix;
    }
}

