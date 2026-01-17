package com.zorroe.cloud.filebox.common;

import com.zorroe.cloud.filebox.enums.HttpStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 响应状态码（如 200 成功，400 参数错误，500 服务器错误等）
     */
    private Integer code;

    /**
     * 响应消息（如 "操作成功"、"用户不存在"）
     */
    private String message;

    /**
     * 响应数据（泛型，可为任意对象）
     */
    private T data;

    /**
     * 构造方法：私有化，强制使用静态工厂方法
     */
    private Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // ===== 静态工厂方法 =====

    /**
     * 成功：无数据返回
     */
    public static <T> Result<T> success() {
        return new Result<>(HttpStatus.SUCCESS.getCode(), HttpStatus.SUCCESS.getMessage(), null);
    }

    /**
     * 成功：带数据返回
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(HttpStatus.SUCCESS.getCode(), HttpStatus.SUCCESS.getMessage(), data);
    }

    /**
     * 成功：自定义消息 + 数据
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(HttpStatus.SUCCESS.getCode(), message, data);
    }

    /**
     * 失败：仅消息
     */
    public static <T> Result<T> fail(String message) {
        return new Result<>(HttpStatus.INTERNAL_ERROR.getCode(), message, null);
    }

    /**
     * 失败：指定状态码和消息
     */
    public static <T> Result<T> fail(Integer code, String message) {
        return new Result<>(code, message, null);
    }

    /**
     * 失败：使用预定义错误类型
     */
    public static <T> Result<T> fail(HttpStatus status) {
        return new Result<>(status.getCode(), status.getMessage(), null);
    }

}
