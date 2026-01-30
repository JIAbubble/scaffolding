package com.example.performanceevaluation.utils;

import java.util.regex.Pattern;

/**
 * 验证工具类
 */
public class ValidationUtil {

    /** 邮箱正则表达式 */
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );

    /** 手机号正则表达式（11位数字，1开头） */
    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^1[3-9]\\d{9}$"
    );

    /** 身份证号正则表达式（18位或15位） */
    private static final Pattern ID_CARD_PATTERN = Pattern.compile(
            "^(\\d{15}|\\d{17}[\\dXx])$"
    );

    /** URL正则表达式 */
    private static final Pattern URL_PATTERN = Pattern.compile(
            "^(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})([/\\w .-]*)*/?$"
    );

    /** IP地址正则表达式 */
    private static final Pattern IP_PATTERN = Pattern.compile(
            "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$"
    );

    /** 中文正则表达式 */
    private static final Pattern CHINESE_PATTERN = Pattern.compile("^[\\u4e00-\\u9fa5]+$");

    /** 数字正则表达式 */
    private static final Pattern NUMBER_PATTERN = Pattern.compile("^\\d+$");

    /** 整数正则表达式（包括负数） */
    private static final Pattern INTEGER_PATTERN = Pattern.compile("^-?\\d+$");

    /** 浮点数正则表达式 */
    private static final Pattern DECIMAL_PATTERN = Pattern.compile("^-?\\d+(\\.\\d+)?$");

    /**
     * 验证邮箱格式
     */
    public static boolean isEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * 验证手机号格式
     */
    public static boolean isPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }

    /**
     * 验证身份证号格式
     */
    public static boolean isIdCard(String idCard) {
        return idCard != null && ID_CARD_PATTERN.matcher(idCard).matches();
    }

    /**
     * 验证URL格式
     */
    public static boolean isUrl(String url) {
        return url != null && URL_PATTERN.matcher(url).matches();
    }

    /**
     * 验证IP地址格式
     */
    public static boolean isIp(String ip) {
        return ip != null && IP_PATTERN.matcher(ip).matches();
    }

    /**
     * 验证是否为中文
     */
    public static boolean isChinese(String str) {
        return str != null && CHINESE_PATTERN.matcher(str).matches();
    }

    /**
     * 验证是否为数字
     */
    public static boolean isNumber(String str) {
        return str != null && NUMBER_PATTERN.matcher(str).matches();
    }

    /**
     * 验证是否为整数
     */
    public static boolean isInteger(String str) {
        return str != null && INTEGER_PATTERN.matcher(str).matches();
    }

    /**
     * 验证是否为浮点数
     */
    public static boolean isDecimal(String str) {
        return str != null && DECIMAL_PATTERN.matcher(str).matches();
    }

    /**
     * 验证字符串长度
     */
    public static boolean isLengthValid(String str, int min, int max) {
        if (str == null) {
            return min == 0;
        }
        int length = str.length();
        return length >= min && length <= max;
    }

    /**
     * 验证字符串长度（固定长度）
     */
    public static boolean isLengthValid(String str, int length) {
        return str != null && str.length() == length;
    }

    /**
     * 验证密码强度（至少包含字母和数字，长度6-20）
     */
    public static boolean isStrongPassword(String password) {
        if (StringUtil.isBlank(password) || password.length() < 6 || password.length() > 20) {
            return false;
        }
        boolean hasLetter = password.matches(".*[a-zA-Z].*");
        boolean hasDigit = password.matches(".*\\d.*");
        return hasLetter && hasDigit;
    }

    /**
     * 验证用户名格式（字母、数字、下划线，3-20位）
     */
    public static boolean isUsername(String username) {
        if (StringUtil.isBlank(username)) {
            return false;
        }
        return username.matches("^[a-zA-Z0-9_]{3,20}$");
    }

    /**
     * 验证是否为有效的日期格式（yyyy-MM-dd）
     */
    public static boolean isDate(String dateStr) {
        if (StringUtil.isBlank(dateStr)) {
            return false;
        }
        try {
            java.time.LocalDate.parse(dateStr, java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 验证是否为有效的日期时间格式（yyyy-MM-dd HH:mm:ss）
     */
    public static boolean isDateTime(String dateTimeStr) {
        if (StringUtil.isBlank(dateTimeStr)) {
            return false;
        }
        try {
            java.time.LocalDateTime.parse(dateTimeStr,
                    java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

