package com.example.performanceevaluation.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * 日期时间工具类
 */
public class DateUtil {

    /** 标准日期时间格式 */
    public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    /** 标准日期格式 */
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    /** 标准时间格式 */
    public static final String TIME_PATTERN = "HH:mm:ss";

    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DATETIME_PATTERN);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

    /**
     * 获取当前日期时间字符串
     */
    public static String now() {
        return LocalDateTime.now().format(DATETIME_FORMATTER);
    }

    /**
     * 获取当前日期字符串
     */
    public static String today() {
        return LocalDate.now().format(DATE_FORMATTER);
    }

    /**
     * 格式化日期时间
     */
    public static String format(LocalDateTime dateTime) {
        return dateTime == null ? null : dateTime.format(DATETIME_FORMATTER);
    }

    /**
     * 格式化日期时间（自定义格式）
     */
    public static String format(LocalDateTime dateTime, String pattern) {
        return dateTime == null ? null : dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 格式化日期
     */
    public static String formatDate(LocalDate date) {
        return date == null ? null : date.format(DATE_FORMATTER);
    }

    /**
     * 解析日期时间字符串
     */
    public static LocalDateTime parse(String dateTimeStr) {
        return dateTimeStr == null ? null : LocalDateTime.parse(dateTimeStr, DATETIME_FORMATTER);
    }

    /**
     * 解析日期字符串
     */
    public static LocalDate parseDate(String dateStr) {
        return dateStr == null ? null : LocalDate.parse(dateStr, DATE_FORMATTER);
    }

    /**
     * LocalDateTime 转 Date
     */
    public static Date toDate(LocalDateTime dateTime) {
        return dateTime == null ? null : Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Date 转 LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        return date == null ? null : LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * 计算两个日期之间的天数差
     */
    public static long daysBetween(LocalDate start, LocalDate end) {
        return ChronoUnit.DAYS.between(start, end);
    }

    /**
     * 计算两个日期时间之间的小时差
     */
    public static long hoursBetween(LocalDateTime start, LocalDateTime end) {
        return ChronoUnit.HOURS.between(start, end);
    }

    /**
     * 计算两个日期时间之间的分钟差
     */
    public static long minutesBetween(LocalDateTime start, LocalDateTime end) {
        return ChronoUnit.MINUTES.between(start, end);
    }

    /**
     * 获取指定天数后的日期
     */
    public static LocalDate plusDays(LocalDate date, long days) {
        return date == null ? null : date.plusDays(days);
    }

    /**
     * 获取指定天数前的日期
     */
    public static LocalDate minusDays(LocalDate date, long days) {
        return date == null ? null : date.minusDays(days);
    }

    /**
     * 判断日期是否在指定范围内
     */
    public static boolean isBetween(LocalDate date, LocalDate start, LocalDate end) {
        return date != null && start != null && end != null
                && !date.isBefore(start) && !date.isAfter(end);
    }

    /**
     * 判断是否为今天
     */
    public static boolean isToday(LocalDate date) {
        return date != null && date.equals(LocalDate.now());
    }

    /**
     * 获取时间戳（秒）
     */
    public static long getTimestamp() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 获取时间戳（毫秒）
     */
    public static long getTimestampMillis() {
        return System.currentTimeMillis();
    }
}

