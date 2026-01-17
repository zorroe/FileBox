package com.zorroe.cloud.filebox.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zorroe.cloud.filebox.entity.File;
import org.springframework.web.multipart.MultipartFile;

public interface FileService extends IService<File> {
    String uploadFile(MultipartFile file, Integer maxDownloadCount, Integer expireValue, String expireStyle);
}
