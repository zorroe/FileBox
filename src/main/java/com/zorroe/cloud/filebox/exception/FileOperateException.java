package com.zorroe.cloud.filebox.exception;

public class FileOperateException extends RuntimeException {

    private final int code;

    public FileOperateException(String message) {
        super(message);
        this.code = 500;
    }

    public FileOperateException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
