package com.yisen.common.exception;

import com.yisen.common.enums.ResponseCodeEnum;

/**
 * 系统异常
 * 用于系统级别的异常情况（如配置错误、服务不可用等）
 *
 * @author yisen
 */
public class SystemException extends BaseException {

    private static final long serialVersionUID = 1L;

    public SystemException() {
        super(ResponseCodeEnum.INTERNAL_SERVER_ERROR);
    }

    public SystemException(String message) {
        super(ResponseCodeEnum.INTERNAL_SERVER_ERROR.getCode(), message);
    }

    public SystemException(Integer code, String message) {
        super(code, message);
    }

    public SystemException(ResponseCodeEnum responseCode) {
        super(responseCode);
    }

    public SystemException(ResponseCodeEnum responseCode, String message) {
        super(responseCode, message);
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public SystemException(Integer code, String message, Throwable cause) {
        super(code, message, cause);
    }

    /**
     * 快速创建系统异常
     */
    public static SystemException of(String message) {
        return new SystemException(message);
    }

    /**
     * 快速创建系统异常（使用枚举）
     */
    public static SystemException of(ResponseCodeEnum responseCode) {
        return new SystemException(responseCode);
    }

    /**
     * 快速创建系统异常（使用枚举和自定义消息）
     */
    public static SystemException of(ResponseCodeEnum responseCode, String message) {
        return new SystemException(responseCode, message);
    }
}
