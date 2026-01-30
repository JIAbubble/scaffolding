package com.example.performanceevaluation.controller;

import com.example.performanceevaluation.constants.SystemConstants;
import com.example.performanceevaluation.exception.BusinessException;
import com.example.performanceevaluation.pojo.ResultCode;
import com.example.performanceevaluation.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.unit.DataSize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 文件上传/下载示例 Controller
 */
@Slf4j
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    /**
     * 文件保存根目录，可在配置文件中通过 file.upload-dir 覆盖
     */
    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    /**
     * 上传单个文件
     */
    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new BusinessException(ResultCode.PARAM_VALIDATE_ERROR, "上传文件不能为空");
        }

        // 大小校验
        long size = file.getSize();
        if (size > SystemConstants.MAX_FILE_SIZE) {
            String maxSizeStr = DataSize.ofBytes(SystemConstants.MAX_FILE_SIZE).toMegabytes() + "MB";
            throw new BusinessException(ResultCode.PARAM_VALIDATE_ERROR, "文件大小不能超过 " + maxSizeStr);
        }

        // 扩展名校验
        String originalFilename = file.getOriginalFilename();
        String ext = getExtension(originalFilename);
        if (!isAllowedExtension(ext)) {
            throw new BusinessException(ResultCode.PARAM_VALIDATE_ERROR, "不支持的文件类型: " + ext);
        }

        // 生成保存文件名：时间戳 + 原始文件名
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String safeName = StringUtil.isBlank(originalFilename) ? "file" : originalFilename.replaceAll("\\s+", "_");
        String filename = timestamp + "_" + safeName;

        Path dir = Paths.get(uploadDir);
        Files.createDirectories(dir);
        Path dest = dir.resolve(filename);
        file.transferTo(dest);

        log.info("文件上传成功: {} -> {}", originalFilename, dest.toAbsolutePath());
        // 返回文件名，前端可用此文件名进行下载
        return filename;
    }

    /**
     * 根据文件名下载文件
     */
    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> download(@PathVariable("filename") String filename) throws MalformedURLException {
        if (StringUtil.isBlank(filename)) {
            throw new BusinessException(ResultCode.PARAM_VALIDATE_ERROR, "文件名不能为空");
        }

        Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
        Resource resource = new UrlResource(filePath.toUri());
        if (!resource.exists() || !resource.isReadable()) {
            throw new BusinessException(ResultCode.DATA_NOT_EXIST, "文件不存在");
        }

        String contentType = "application/octet-stream";
        try {
            String probe = Files.probeContentType(filePath);
            if (probe != null) {
                contentType = probe;
            }
        } catch (IOException e) {
            log.warn("探测文件类型失败: {}", filePath, e);
        }

        String encodedFilename = URLEncoder.encode(resource.getFilename(), StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename*=UTF-8''" + encodedFilename)
                .body(resource);
    }

    private String getExtension(String filename) {
        if (StringUtil.isBlank(filename) || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
    }

    private boolean isAllowedExtension(String ext) {
        if (StringUtil.isBlank(ext)) {
            return false;
        }
        for (String allowed : SystemConstants.ALLOWED_FILE_EXTENSIONS) {
            if (allowed.equalsIgnoreCase(ext)) {
                return true;
            }
        }
        return false;
    }
}


