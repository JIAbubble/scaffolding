package com.example.performanceevaluation.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * 加密工具类
 */
public class SecurityUtil {

    /**
     * MD5加密
     */
    public static String md5(String input) {
        if (StringUtil.isBlank(input)) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5算法不可用", e);
        }
    }

    /**
     * SHA256加密
     */
    public static String sha256(String input) {
        if (StringUtil.isBlank(input)) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256算法不可用", e);
        }
    }

    /**
     * SHA512加密
     */
    public static String sha512(String input) {
        if (StringUtil.isBlank(input)) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-512算法不可用", e);
        }
    }

    /**
     * Base64编码
     */
    public static String base64Encode(String input) {
        if (input == null) {
            return null;
        }
        return Base64.getEncoder().encodeToString(input.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Base64编码（字节数组）
     */
    public static String base64Encode(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * Base64解码
     */
    public static String base64Decode(String encoded) {
        if (StringUtil.isBlank(encoded)) {
            return null;
        }
        try {
            byte[] decoded = Base64.getDecoder().decode(encoded);
            return new String(decoded, StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Base64解码失败", e);
        }
    }

    /**
     * Base64解码（返回字节数组）
     */
    public static byte[] base64DecodeToBytes(String encoded) {
        if (StringUtil.isBlank(encoded)) {
            return null;
        }
        try {
            return Base64.getDecoder().decode(encoded);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Base64解码失败", e);
        }
    }

    /**
     * 字节数组转十六进制字符串
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * 生成随机字符串（用于盐值、token等）
     */
    public static String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    /**
     * 生成随机数字字符串
     */
    public static String generateRandomNumber(int length) {
        StringBuilder sb = new StringBuilder();
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    /**
     * 密码加盐加密（MD5 + Salt）
     */
    public static String hashPassword(String password, String salt) {
        if (StringUtil.isBlank(password) || StringUtil.isBlank(salt)) {
            return null;
        }
        return md5(password + salt);
    }

    /**
     * 验证密码
     */
    public static boolean verifyPassword(String password, String salt, String hashedPassword) {
        if (StringUtil.isBlank(password) || StringUtil.isBlank(salt) || StringUtil.isBlank(hashedPassword)) {
            return false;
        }
        String hash = hashPassword(password, salt);
        return hash != null && hash.equals(hashedPassword);
    }
}

