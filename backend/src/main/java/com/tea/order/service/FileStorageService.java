package com.tea.order.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path root;

    public FileStorageService(@Value("${app.upload.dir}") String uploadDir) {
        this.root = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new IllegalStateException("无法创建上传目录: " + root, e);
        }
    }

    public String store(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new com.tea.order.common.BusinessException("文件不能为空");
        }
        String original = file.getOriginalFilename();
        String ext = "";
        int dot = original == null ? -1 : original.lastIndexOf('.');
        if (dot >= 0) ext = original.substring(dot).toLowerCase();
        String filename = UUID.randomUUID().toString().replace("-", "") + ext;
        Path target = root.resolve(filename);
        try {
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new com.tea.order.common.BusinessException("文件保存失败");
        }
        return "/uploads/" + filename;
    }
}
