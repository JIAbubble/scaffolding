package com.example.performanceevaluation.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件工具类
 */
public class FileUtil {

    /**
     * 读取文件内容为字符串
     *
     * @param filePath 文件路径
     * @return 文件内容
     */
    public static String readFile(String filePath) {
        if (StringUtil.isBlank(filePath)) {
            return null;
        }
        try {
            return Files.readString(Paths.get(filePath));
        } catch (IOException e) {
            throw new RuntimeException("读取文件失败: " + filePath, e);
        }
    }

    /**
     * 读取文件内容为字符串列表（按行）
     *
     * @param filePath 文件路径
     * @return 文件内容列表
     */
    public static List<String> readLines(String filePath) {
        if (StringUtil.isBlank(filePath)) {
            return new ArrayList<>();
        }
        try {
            return Files.readAllLines(Paths.get(filePath));
        } catch (IOException e) {
            throw new RuntimeException("读取文件失败: " + filePath, e);
        }
    }

    /**
     * 写入文件内容
     *
     * @param filePath 文件路径
     * @param content  内容
     */
    public static void writeFile(String filePath, String content) {
        if (StringUtil.isBlank(filePath)) {
            return;
        }
        try {
            Path path = Paths.get(filePath);
            // 确保父目录存在
            Files.createDirectories(path.getParent());
            Files.writeString(path, content);
        } catch (IOException e) {
            throw new RuntimeException("写入文件失败: " + filePath, e);
        }
    }

    /**
     * 追加文件内容
     *
     * @param filePath 文件路径
     * @param content  内容
     */
    public static void appendFile(String filePath, String content) {
        if (StringUtil.isBlank(filePath)) {
            return;
        }
        try {
            Path path = Paths.get(filePath);
            // 确保父目录存在
            Files.createDirectories(path.getParent());
            Files.writeString(path, content, java.nio.file.StandardOpenOption.CREATE,
                    java.nio.file.StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException("追加文件失败: " + filePath, e);
        }
    }

    /**
     * 复制文件
     *
     * @param sourcePath 源文件路径
     * @param targetPath 目标文件路径
     */
    public static void copyFile(String sourcePath, String targetPath) {
        if (StringUtil.isBlank(sourcePath) || StringUtil.isBlank(targetPath)) {
            return;
        }
        try {
            Path source = Paths.get(sourcePath);
            Path target = Paths.get(targetPath);
            // 确保目标目录存在
            Files.createDirectories(target.getParent());
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("复制文件失败: " + sourcePath + " -> " + targetPath, e);
        }
    }

    /**
     * 移动文件
     *
     * @param sourcePath 源文件路径
     * @param targetPath 目标文件路径
     */
    public static void moveFile(String sourcePath, String targetPath) {
        if (StringUtil.isBlank(sourcePath) || StringUtil.isBlank(targetPath)) {
            return;
        }
        try {
            Path source = Paths.get(sourcePath);
            Path target = Paths.get(targetPath);
            // 确保目标目录存在
            Files.createDirectories(target.getParent());
            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("移动文件失败: " + sourcePath + " -> " + targetPath, e);
        }
    }

    /**
     * 删除文件
     *
     * @param filePath 文件路径
     */
    public static boolean deleteFile(String filePath) {
        if (StringUtil.isBlank(filePath)) {
            return false;
        }
        try {
            return Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            throw new RuntimeException("删除文件失败: " + filePath, e);
        }
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath 文件路径
     * @return 是否存在
     */
    public static boolean exists(String filePath) {
        if (StringUtil.isBlank(filePath)) {
            return false;
        }
        return Files.exists(Paths.get(filePath));
    }

    /**
     * 判断是否为文件
     *
     * @param filePath 文件路径
     * @return 是否为文件
     */
    public static boolean isFile(String filePath) {
        if (StringUtil.isBlank(filePath)) {
            return false;
        }
        return Files.isRegularFile(Paths.get(filePath));
    }

    /**
     * 判断是否为目录
     *
     * @param dirPath 目录路径
     * @return 是否为目录
     */
    public static boolean isDirectory(String dirPath) {
        if (StringUtil.isBlank(dirPath)) {
            return false;
        }
        return Files.isDirectory(Paths.get(dirPath));
    }

    /**
     * 创建目录
     *
     * @param dirPath 目录路径
     */
    public static void createDirectory(String dirPath) {
        if (StringUtil.isBlank(dirPath)) {
            return;
        }
        try {
            Files.createDirectories(Paths.get(dirPath));
        } catch (IOException e) {
            throw new RuntimeException("创建目录失败: " + dirPath, e);
        }
    }

    /**
     * 获取文件大小（字节）
     *
     * @param filePath 文件路径
     * @return 文件大小
     */
    public static long getFileSize(String filePath) {
        if (StringUtil.isBlank(filePath)) {
            return 0;
        }
        try {
            return Files.size(Paths.get(filePath));
        } catch (IOException e) {
            throw new RuntimeException("获取文件大小失败: " + filePath, e);
        }
    }

    /**
     * 获取文件扩展名
     *
     * @param fileName 文件名
     * @return 扩展名（不含点）
     */
    public static String getExtension(String fileName) {
        if (StringUtil.isBlank(fileName)) {
            return "";
        }
        int lastDot = fileName.lastIndexOf('.');
        return lastDot == -1 ? "" : fileName.substring(lastDot + 1).toLowerCase();
    }

    /**
     * 获取文件名（不含扩展名）
     *
     * @param fileName 文件名
     * @return 文件名（不含扩展名）
     */
    public static String getNameWithoutExtension(String fileName) {
        if (StringUtil.isBlank(fileName)) {
            return "";
        }
        int lastDot = fileName.lastIndexOf('.');
        return lastDot == -1 ? fileName : fileName.substring(0, lastDot);
    }

    /**
     * 格式化文件大小
     *
     * @param size 文件大小（字节）
     * @return 格式化后的字符串
     */
    public static String formatFileSize(long size) {
        if (size < 1024) {
            return size + " B";
        } else if (size < 1024 * 1024) {
            return String.format("%.2f KB", size / 1024.0);
        } else if (size < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", size / (1024.0 * 1024.0));
        } else {
            return String.format("%.2f GB", size / (1024.0 * 1024.0 * 1024.0));
        }
    }
}

