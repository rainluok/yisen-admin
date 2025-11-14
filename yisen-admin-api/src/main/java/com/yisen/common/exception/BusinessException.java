package com.yisen.common.exception;

import com.yisen.common.enums.ResponseCodeEnum;

/**
 * 业务异常
 * 用于业务逻辑处理中的异常情况
 *
 * @author yisen
 */
public class BusinessException extends BaseException {

    private static final long serialVersionUID = 1L;

    public BusinessException() {
        super(ResponseCodeEnum.BUSINESS_ERROR);
    }

    public BusinessException(String message) {
        super(ResponseCodeEnum.BUSINESS_ERROR.getCode(), message);
    }

    public BusinessException(Integer code, String message) {
        super(code, message);
    }

    public BusinessException(ResponseCodeEnum responseCode) {
        super(responseCode);
    }

    public BusinessException(ResponseCodeEnum responseCode, String message) {
        super(responseCode, message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(Integer code, String message, Throwable cause) {
        super(code, message, cause);
    }

    /**
     * 快速创建业务异常（用于断言）
     */
    public static BusinessException of(String message) {
        return new BusinessException(message);
    }

    /**
     * 快速创建业务异常（使用枚举）
     */
    public static BusinessException of(ResponseCodeEnum responseCode) {
        return new BusinessException(responseCode);
    }

    /**
     * 快速创建业务异常（使用枚举和自定义消息）
     */
    public static BusinessException of(ResponseCodeEnum responseCode, String message) {
        return new BusinessException(responseCode, message);
    }
}

