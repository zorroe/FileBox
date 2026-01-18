package com.zorroe.cloud.filebox.aspect;


import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zorroe.cloud.filebox.entity.File;
import com.zorroe.cloud.filebox.entity.Log;
import com.zorroe.cloud.filebox.enums.LogStatusEnum;
import com.zorroe.cloud.filebox.enums.ServerStatus;
import com.zorroe.cloud.filebox.exception.FileOperateException;
import com.zorroe.cloud.filebox.service.FileService;
import com.zorroe.cloud.filebox.service.LogService;
import com.zorroe.cloud.filebox.utils.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;

@Slf4j
@Aspect
@Component
public class DownloadLogAspect {

    @Resource
    private LogService logService;

    @Resource
    private FileService fileService;

    @Pointcut("execution(* com.zorroe.cloud.filebox.controller.FileController.downloadFile(..))")
    public void downloadRequest() {
    }

    @Around("downloadRequest()")
    public Object logDownloadRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        Log downloadLog = new Log();
        downloadLog.setAction("DOWNLOAD");

        File file = new File();

        downloadLog.setTimestamp(new Date());
        // 获取 HttpServletRequest
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;


        if (request != null) {
            // 获取 IP
            String ip = IpUtil.getClientIp(request);
            downloadLog.setIp(ip);

            // 获取 code（从方法参数）
            Object[] args = joinPoint.getArgs();
            if (args.length > 0 && args[0] instanceof String) {
                file.setCode((String) args[0]);
            }
        }

        File one = fileService.getOne(new LambdaQueryWrapper<File>().eq(File::getCode, file.getCode()));
        if (Objects.isNull(one)) {
            throw new FileOperateException(ServerStatus.FILE_NOT_FOUND.getCode(), ServerStatus.FILE_NOT_FOUND.getMessage());
        }
        downloadLog.setDetails(JSONUtil.toJsonStr(one));

        Object result;
        try {
            // 执行原方法
            result = joinPoint.proceed();

            // 判断是否成功（根据返回值类型）
            if (result instanceof org.springframework.http.ResponseEntity) {
                org.springframework.http.ResponseEntity<?> response = (org.springframework.http.ResponseEntity<?>) result;
                if (response.getStatusCode().is2xxSuccessful()) {
                    downloadLog.setStatus(LogStatusEnum.SUCCESS.getCode());
                } else {
                    downloadLog.setStatus(LogStatusEnum.FAILURE.getCode());
                }
            } else {
                downloadLog.setStatus(LogStatusEnum.FAILURE.getCode());
            }
        } catch (Exception e) {
            downloadLog.setStatus(LogStatusEnum.FAILURE.getCode());
            throw e; // 重新抛出异常，不影响原有流程
        } finally {
            saveLogAsync(downloadLog);
        }
        return result;
    }

    private void saveLogAsync(Log downloadLog) {
        new Thread(() -> {
            try {
                logService.save(downloadLog);
            } catch (Exception e) {
                // 日志保存失败不应影响主业务
                LoggerFactory.getLogger(DownloadLogAspect.class)
                        .error("Failed to save download log to DB", e);
            }
        }).start();
    }
}
