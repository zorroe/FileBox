package com.zorroe.cloud.filebox.service.impl;

import com.zorroe.cloud.filebox.service.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Slf4j
@Service
public class FileStorageServiceImpl implements FileStorageService {


    @Value("${file.storage.location:./uploads}")
    private String storageLocation;

    @Override
    public String storeFile(MultipartFile file) {

        Path filePath;

        try {
            Path storagePath = Paths.get(storageLocation).toAbsolutePath().normalize();
            Files.createDirectories(storagePath);

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            filePath = storagePath.resolve(fileName);

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            log.error("文件保存失败: {}", e.getMessage());
            throw new RuntimeException("文件保存失败", e);
        }

        return filePath.toString();
    }

    @Override
    public byte[] getFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return Files.readAllBytes(path);
    }

    @Override
    public Path getFilePath(String fileName) {
        return Paths.get(storageLocation).resolve(fileName);
    }
}
