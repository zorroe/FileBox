package com.zorroe.cloud.filebox.enums;

import lombok.Getter;

/**
 * 常用 HTTP 状态码与业务状态码封装
 */
@Getter
public enum ServerStatus {

    // 成功
    SUCCESS(200, "操作成功"),

    // 客户端错误
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未认证（登录失效）"),
    FORBIDDEN(403, "无权限访问"),
    NOT_FOUND(404, "资源不存在"),

    // 业务逻辑错误（可自定义）
    VALIDATION_ERROR(4001, "参数校验失败"),
    USER_NOT_FOUND(4002, "用户不存在"),
    DUPLICATE_DATA(4003, "数据已存在"),

    // 服务器错误
    INTERNAL_ERROR(500, "服务器内部错误"),
    SERVICE_UNAVAILABLE(503, "服务暂不可用"),

    // 文件相关错误
    FILE_NOT_FOUND(5004, "文件不存在"),
    FILE_UPLOAD_ERROR(5005, "文件上传失败"),
    FILE_DOWNLOAD_ERROR(5006, "文件下载失败"),
    FILE_DELETE_ERROR(5007, "文件删除失败"),
    FILE_EXPIRED(5008, "文件已过期"),
    FILE_DELETED(5009, "文件已被删除");


    private final int code;
    private final String message;

    ServerStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
