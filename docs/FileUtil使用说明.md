# FileUtil 文件工具类使用说明

## 📋 概述

`FileUtil` 提供了文件读写、复制、移动、删除等常用文件操作方法。

## 🔧 常用方法

### 1. 文件读取

```java
// 读取文件内容为字符串
String content = FileUtil.readFile("D:/data/file.txt");

// 读取文件内容为字符串列表（按行）
List<String> lines = FileUtil.readLines("D:/data/file.txt");
```

### 2. 文件写入

```java
// 写入文件内容（覆盖）
String content = "Hello World\nThis is a test file";
FileUtil.writeFile("D:/data/output.txt", content);

// 追加文件内容
String appendContent = "New line";
FileUtil.appendFile("D:/data/output.txt", appendContent);
```

### 3. 文件复制和移动

```java
// 复制文件
FileUtil.copyFile("D:/data/source.txt", "D:/data/target.txt");

// 移动文件
FileUtil.moveFile("D:/data/source.txt", "D:/data/moved.txt");
```

### 4. 文件删除

```java
// 删除文件
boolean deleted = FileUtil.deleteFile("D:/data/file.txt");
// 返回 true 表示删除成功，false 表示文件不存在
```

### 5. 文件判断

```java
// 判断文件是否存在
boolean exists = FileUtil.exists("D:/data/file.txt");

// 判断是否为文件
boolean isFile = FileUtil.isFile("D:/data/file.txt");

// 判断是否为目录
boolean isDir = FileUtil.isDirectory("D:/data");
```

### 6. 目录操作

```java
// 创建目录（包括父目录）
FileUtil.createDirectory("D:/data/subdir/nested");
```

### 7. 文件信息获取

```java
// 获取文件大小（字节）
long size = FileUtil.getFileSize("D:/data/file.txt");

// 获取文件扩展名
String ext = FileUtil.getExtension("document.pdf");  // "pdf"
String ext2 = FileUtil.getExtension("file");         // ""

// 获取文件名（不含扩展名）
String name = FileUtil.getNameWithoutExtension("document.pdf");  // "document"
```

### 8. 文件大小格式化

```java
// 格式化文件大小
String size1 = FileUtil.formatFileSize(1024);           // "1.00 KB"
String size2 = FileUtil.formatFileSize(1048576);      // "1.00 MB"
String size3 = FileUtil.formatFileSize(1073741824);     // "1.00 GB"
```

## 💡 实际应用示例

### 示例1：读取配置文件

```java
@Service
public class ConfigService {
    
    public Map<String, String> loadConfig(String configPath) {
        // 读取配置文件
        String content = FileUtil.readFile(configPath);
        
        // 解析配置（假设是 key=value 格式）
        Map<String, String> config = new HashMap<>();
        List<String> lines = FileUtil.readLines(configPath);
        for (String line : lines) {
            if (StringUtil.isNotBlank(line) && line.contains("=")) {
                String[] parts = line.split("=", 2);
                config.put(parts[0].trim(), parts[1].trim());
            }
        }
        
        return config;
    }
}
```

### 示例2：日志文件写入

```java
@Service
public class LogService {
    
    public void writeLog(String message) {
        String logFile = "logs/application.log";
        String timestamp = DateUtil.now();
        String logEntry = String.format("[%s] %s\n", timestamp, message);
        
        // 追加日志
        FileUtil.appendFile(logFile, logEntry);
    }
}
```

### 示例3：文件上传处理

```java
@RestController
public class FileController {
    
    @PostMapping("/upload")
    public ResponseResult<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // 获取文件信息
            String originalFilename = file.getOriginalFilename();
            String extension = FileUtil.getExtension(originalFilename);
            String fileName = FileUtil.getNameWithoutExtension(originalFilename);
            
            // 生成新文件名
            String newFileName = fileName + "_" + System.currentTimeMillis() + "." + extension;
            String savePath = "uploads/" + newFileName;
            
            // 保存文件
            FileUtil.writeFile(savePath, new String(file.getBytes()));
            
            return ResponseResult.success("文件上传成功: " + savePath);
        } catch (Exception e) {
            return ResponseResult.fail("文件上传失败: " + e.getMessage());
        }
    }
}
```

### 示例4：文件备份

```java
@Service
public class BackupService {
    
    public void backupFile(String filePath) {
        // 生成备份文件名
        String backupPath = filePath + ".backup." + DateUtil.getTimestamp();
        
        // 复制文件
        FileUtil.copyFile(filePath, backupPath);
        
        log.info("文件备份成功: {} -> {}", filePath, backupPath);
    }
    
    public void restoreFile(String backupPath, String targetPath) {
        // 恢复文件
        FileUtil.copyFile(backupPath, targetPath);
        
        log.info("文件恢复成功: {} -> {}", backupPath, targetPath);
    }
}
```

### 示例5：文件清理

```java
@Service
public class FileCleanupService {
    
    public void cleanupOldFiles(String directory, int daysToKeep) {
        // 获取目录下所有文件
        // 这里需要自己实现文件列表获取
        
        // 检查文件修改时间，删除旧文件
        // ...
    }
    
    public void deleteFileIfExists(String filePath) {
        if (FileUtil.exists(filePath)) {
            FileUtil.deleteFile(filePath);
            log.info("文件已删除: {}", filePath);
        }
    }
}
```

### 示例6：文件信息展示

```java
@RestController
public class FileInfoController {
    
    @GetMapping("/file/info")
    public ResponseResult<Map<String, Object>> getFileInfo(@RequestParam String filePath) {
        Map<String, Object> info = new HashMap<>();
        
        if (!FileUtil.exists(filePath)) {
            return ResponseResult.fail("文件不存在");
        }
        
        info.put("exists", true);
        info.put("isFile", FileUtil.isFile(filePath));
        info.put("isDirectory", FileUtil.isDirectory(filePath));
        
        if (FileUtil.isFile(filePath)) {
            long size = FileUtil.getFileSize(filePath);
            info.put("size", size);
            info.put("sizeFormatted", FileUtil.formatFileSize(size));
            info.put("extension", FileUtil.getExtension(filePath));
            info.put("nameWithoutExtension", FileUtil.getNameWithoutExtension(filePath));
        }
        
        return ResponseResult.success(info);
    }
}
```

### 示例7：批量文件处理

```java
@Service
public class BatchFileService {
    
    public void processFiles(List<String> filePaths) {
        for (String filePath : filePaths) {
            try {
                // 读取文件
                String content = FileUtil.readFile(filePath);
                
                // 处理内容
                String processed = processContent(content);
                
                // 写入新文件
                String outputPath = filePath + ".processed";
                FileUtil.writeFile(outputPath, processed);
                
                log.info("文件处理完成: {}", filePath);
            } catch (Exception e) {
                log.error("文件处理失败: {}", filePath, e);
            }
        }
    }
    
    private String processContent(String content) {
        // 处理逻辑
        return content.toUpperCase();
    }
}
```

## ⚠️ 注意事项

1. **路径问题**：Windows 和 Linux 路径分隔符不同，建议使用相对路径或配置路径
2. **权限问题**：确保应用有文件读写权限
3. **异常处理**：文件操作可能抛出异常，建议使用 try-catch
4. **性能考虑**：大文件操作时注意内存使用
5. **并发安全**：多线程环境下注意文件锁问题

## 🔧 路径处理建议

```java
// 使用相对路径（相对于项目根目录）
String filePath = "data/files/document.txt";

// 使用系统属性
String userHome = System.getProperty("user.home");
String filePath = userHome + "/data/file.txt";

// 使用配置的路径
@Value("${app.file.upload-path}")
private String uploadPath;
```

## 🔗 相关工具类

- `StringUtil` - 字符串处理
- `DateUtil` - 日期时间处理（用于生成文件名）

