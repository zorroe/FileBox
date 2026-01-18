package com.zorroe.cloud.filebox.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zorroe.cloud.filebox.common.Result;
import com.zorroe.cloud.filebox.dto.FileDto;
import com.zorroe.cloud.filebox.entity.File;
import com.zorroe.cloud.filebox.enums.FileStatusEnum;
import com.zorroe.cloud.filebox.enums.ServerStatus;
import com.zorroe.cloud.filebox.exception.FileOperateException;
import com.zorroe.cloud.filebox.service.FileService;
import com.zorroe.cloud.filebox.service.FileStorageService;
import com.zorroe.cloud.filebox.utils.IpUtil;
import com.zorroe.cloud.filebox.utils.RateLimiterUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.util.Date;
import java.util.Objects;

@RestController
@RequestMapping("/api/file")
public class FileController {

    @Resource
    private FileService fileService;

    @Resource
    private FileStorageService fileStorageService;

    @Value("${file.storage.host:http://localhost:8080}")
    private String storageHost;

    @Resource
    private RateLimiterUtil rateLimiter;

    // 限流配置（可提取到配置文件）
    private static final int MAX_DOWNLOAD_PER_CODE_PER_MINUTE = 2;   // 每个取件码每分钟最多2次
    private static final int MAX_DOWNLOAD_PER_IP_PER_MINUTE = 2;    // 每个IP每分钟最多2次


    /**
     * 上传文件接口
     *
     * @param file        文件
     * @param expireValue 有效期数值
     * @param expireStyle 有效期类型 (day/hour/minute/count/forever)
     * @return 包含取件码和文件名的响应
     */
    @PostMapping("/upload")
    public Result<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "maxDownloadCount", defaultValue = "0") Integer maxDownloadCount,
            @RequestParam(value = "expireValue", defaultValue = "1") Integer expireValue,
            @RequestParam(value = "expireStyle", defaultValue = "days") String expireStyle) {
        String code = fileService.uploadFile(file, maxDownloadCount, expireValue, expireStyle);
        return Result.success(code);
    }

    /**
     * 下载文件接口
     *
     * @param code 取件码
     * @return 文件内容
     */
    @GetMapping("/download/{code}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable("code") String code, HttpServletRequest request) {
        String clientIp = IpUtil.getClientIp(request);
        // 1. 对取件码限流
        if (!rateLimiter.tryAcquire("code:" + code, MAX_DOWNLOAD_PER_CODE_PER_MINUTE, 60)) {
            throw new FileOperateException(ServerStatus.FILE_DOWNLOAD_RATE_EXCEED.getCode(), ServerStatus.FILE_DOWNLOAD_RATE_EXCEED.getMessage());
        }

        // 2. 对 IP 限流
        if (!rateLimiter.tryAcquire("ip:" + clientIp, MAX_DOWNLOAD_PER_IP_PER_MINUTE, 60)) {
            throw new FileOperateException(ServerStatus.FILE_DOWNLOAD_RATE_EXCEED.getCode(), ServerStatus.FILE_DOWNLOAD_RATE_EXCEED.getMessage());
        }

        // 1. 查询文件信息
        LambdaQueryWrapper<File> queryWrapper = new LambdaQueryWrapper<File>().eq(File::getCode, code);
        File file = fileService.getOne(queryWrapper);
        if (Objects.isNull(file)) {
            throw new FileOperateException(ServerStatus.FILE_NOT_FOUND.getCode(), ServerStatus.FILE_NOT_FOUND.getMessage());
        }

        // 2. 检查是否过期
        if (file.getExpireTime() != null && file.getExpireTime().before(new Date())) {
            file.setStatus(FileStatusEnum.EXPIRED.getCode());
            fileService.update(file, new UpdateWrapper<File>().eq("id", file.getId()));
            fileStorageService.deleteFile(file.getStoragePath());
            throw new FileOperateException(ServerStatus.FILE_EXPIRED.getCode(), ServerStatus.FILE_EXPIRED.getMessage());
        }

        // 3. 检查下载次数限制
        if (file.getMaxDownloadCount() > 0 && file.getDownloadCount() >= file.getMaxDownloadCount()) {
            file.setStatus(FileStatusEnum.DELETED.getCode());
            fileService.update(file, new UpdateWrapper<File>().eq("id", file.getId()));
            fileStorageService.deleteFile(file.getStoragePath());
            throw new FileOperateException(ServerStatus.FILE_DOWNLOAD_TIMES_EXCEED.getCode(), ServerStatus.FILE_DOWNLOAD_TIMES_EXCEED.getMessage());
        }
        try {
            // 4. 更新下载次数
            file.setDownloadCount(file.getDownloadCount() + 1);
            fileService.update(file, new UpdateWrapper<File>().eq("id", file.getId()));

            // 5. 构建文件流响应
            FileInputStream fis = new FileInputStream(file.getStoragePath());
            InputStreamResource resource = new InputStreamResource(fis);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(file.getSize())
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +
                            java.net.URLEncoder.encode(file.getName(), "UTF-8").replace("+", "%20") + "\"")
                    .body(resource);
        } catch (Exception e) {
            throw new FileOperateException(ServerStatus.FILE_DOWNLOAD_ERROR.getCode(), ServerStatus.FILE_DOWNLOAD_ERROR.getMessage());
        }
    }

    @GetMapping("/info/{code}")
    public Result<FileDto> getFileInfo(@PathVariable("code") String code) {
        File file = fileService.getOne(new LambdaQueryWrapper<File>().eq(File::getCode, code));
        if (Objects.isNull(file)) {
            throw new FileOperateException(ServerStatus.FILE_NOT_FOUND.getCode(), ServerStatus.FILE_NOT_FOUND.getMessage());
        }
        if (file.getStatus().equals(FileStatusEnum.DELETED.getCode())) {
            throw new FileOperateException(ServerStatus.FILE_DELETED.getCode(), ServerStatus.FILE_DELETED.getMessage());
        }
        if (file.getStatus().equals(FileStatusEnum.EXPIRED.getCode())) {
            throw new FileOperateException(ServerStatus.FILE_EXPIRED.getCode(), ServerStatus.FILE_EXPIRED.getMessage());
        }
        FileDto fileDto = BeanUtil.copyProperties(file, FileDto.class);
        fileDto.setDirectLink(CharSequenceUtil.format("{}/api/file/download/{}", storageHost, file.getCode()));
        return Result.success(fileDto);
    }
}
