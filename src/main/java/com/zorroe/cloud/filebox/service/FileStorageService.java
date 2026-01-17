package com.zorroe.cloud.filebox.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public interface FileStorageService {

    String storeFile(MultipartFile file);

    byte[] getFile(String filePath) throws IOException;

    Path getFilePath(String fileName);

    void deleteFile(String storagePath);
}
