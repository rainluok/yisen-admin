package com.yisen.common.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yisen.common.enums.ResponseCodeEnum;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 统一响应结果
 *
 * @author yisen
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Boolean success;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 时间戳
     */
    private Long timestamp;

    public Result() {
        this.timestamp = System.currentTimeMillis();
    }

    public Result(Boolean success, Integer code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    public Result(Boolean success, Integer code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 成功响应（无数据）
     */
    public static <T> Result<T> success() {
        return new Result<>(true, ResponseCodeEnum.SUCCESS.getCode(), ResponseCodeEnum.SUCCESS.getMessage());
    }

    /**
     * 成功响应（带数据）
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(true, ResponseCodeEnum.SUCCESS.getCode(), ResponseCodeEnum.SUCCESS.getMessage(), data);
    }

    /**
     * 成功响应（自定义消息）
     */
    public static <T> Result<T> success(String message) {
        return new Result<>(true, ResponseCodeEnum.SUCCESS.getCode(), message);
    }

    /**
     * 成功响应（自定义消息和数据）
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(true, ResponseCodeEnum.SUCCESS.getCode(), message, data);
    }

    /**
     * 失败响应（默认消息）
     */
    public static <T> Result<T> fail() {
        return new Result<>(false, ResponseCodeEnum.FAIL.getCode(), ResponseCodeEnum.FAIL.getMessage());
    }

    /**
     * 失败响应（自定义消息）
     */
    public static <T> Result<T> fail(String message) {
        return new Result<>(false, ResponseCodeEnum.FAIL.getCode(), message);
    }

    /**
     * 失败响应（状态码枚举）
     */
    public static <T> Result<T> fail(ResponseCodeEnum responseCode) {
        return new Result<>(false, responseCode.getCode(), responseCode.getMessage());
    }

    /**
     * 失败响应（状态码和消息）
     */
    public static <T> Result<T> fail(Integer code, String message) {
        return new Result<>(false, code, message);
    }

    /**
     * 失败响应（状态码枚举和自定义消息）
     */
    public static <T> Result<T> fail(ResponseCodeEnum responseCode, String message) {
        return new Result<>(false, responseCode.getCode(), message);
    }

    /**
     * 根据布尔值返回成功或失败
     */
    public static <T> Result<T> result(boolean flag) {
        return flag ? success() : fail();
    }

    /**
     * 根据布尔值返回成功或失败（带数据）
     */
    public static <T> Result<T> result(boolean flag, T data) {
        return flag ? success(data) : fail();
    }
}
