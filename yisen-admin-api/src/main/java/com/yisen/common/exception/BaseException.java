package com.yisen.common.exception;

import com.yisen.common.enums.ResponseCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 基础异常类
 *
 * @author yisen
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误消息
     */
    private String message;

    public BaseException() {
        super();
    }

    public BaseException(String message) {
        super(message);
        this.message = message;
    }

    public BaseException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BaseException(ResponseCodeEnum responseCode) {
        super(responseCode.getMessage());
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
    }

    public BaseException(ResponseCodeEnum responseCode, String message) {
        super(message);
        this.code = responseCode.getCode();
        this.message = message;
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    public BaseException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

