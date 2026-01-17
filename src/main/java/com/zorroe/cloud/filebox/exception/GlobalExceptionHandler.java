package com.zorroe.cloud.filebox.exception;

import cn.hutool.http.HttpStatus;
import com.zorroe.cloud.filebox.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(FileOperateException.class)
    public Result<Void> handleFileOperateException(FileOperateException e, HttpServletRequest request) {
        log.error("文件操作异常 [{}]: {}", request.getRequestURI(), e.getMessage(), e);
        return Result.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<Void> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        log.error("请求方法不支持 [{}]: {}", request.getRequestURI(), e.getMessage(), e);
        return Result.fail(HttpStatus.HTTP_BAD_METHOD, e.getMessage());
    }

    // 6. 处理所有其他未捕获异常（兜底）
    @ExceptionHandler(Exception.class)
    public Result<Void> handleUnexpectedException(Exception e, HttpServletRequest request) {
        log.error("系统内部异常 [{}]: {}", request.getRequestURI(), e.getMessage(), e);
        return Result.fail(500, "系统异常，请稍后重试");
    }
}
