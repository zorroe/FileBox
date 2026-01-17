package com.zorroe.cloud.filebox.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zorroe.cloud.filebox.entity.File;
import com.zorroe.cloud.filebox.enums.FileStatusEnum;
import com.zorroe.cloud.filebox.enums.FileTypeEnum;
import com.zorroe.cloud.filebox.mapper.FileMapper;
import com.zorroe.cloud.filebox.service.FileService;
import com.zorroe.cloud.filebox.service.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements FileService {

    @Resource
    private FileStorageService fileStorageService;

    @Override
    public String uploadFile(MultipartFile file, Integer maxDownloadCount, Integer expireValue, String expireStyle) {
        // 生成唯一提取码
        String code = generateUniqueCode();

        // 创建文件对象
        File fileObj = new File();
        fileObj.setCode(code);
        fileObj.setName(file.getOriginalFilename());
        fileObj.setType(FileTypeEnum.FILE.getCode());
        fileObj.setSize(file.getSize());
        fileObj.setMaxDownloadCount(maxDownloadCount);
        fileObj.setStoragePath(fileStorageService.storeFile(file));
        fileObj.setCreateTime(new Date());
        fileObj.setUpdateTime(new Date());
        fileObj.setStatus(FileStatusEnum.ACTIVE.getCode());
        fileObj.setExpireTime(calculateExpireTime(expireValue, expireStyle));

        // 保存文件信息
        save(fileObj);
        return code;
    }

    private Date calculateExpireTime(Integer expireValue, String expireStyle) {
        long expireTime = System.currentTimeMillis();

        switch (expireStyle) {
            case "minutes":
                expireTime += expireValue * 60 * 1000;
                break;
            case "hours":
                expireTime += expireValue * 60 * 60 * 1000;
                break;
            case "days":
                expireTime += expireValue * 24 * 60 * 60 * 1000;
                break;
            case "weeks":
                expireTime += expireValue * 7 * 24 * 60 * 60 * 1000;
                break;
            case "months":
                expireTime += expireValue * 30 * 24 * 60 * 60 * 1000;
                break;
            case "years":
                expireTime += expireValue * 365 * 24 * 60 * 60 * 1000;
                break;
            default:
                expireTime += 24 * 60 * 60 * 1000; // 默认1天
        }
        return new Date(expireTime);
    }

    private String generateUniqueCode() {
        String code;
        do {
            code = UUID.randomUUID().toString().replace("-", "").substring(0, 5).toUpperCase();
        } while (getOne(new LambdaQueryWrapper<File>().eq(File::getCode, code)) != null);
        return code;
    }
}
