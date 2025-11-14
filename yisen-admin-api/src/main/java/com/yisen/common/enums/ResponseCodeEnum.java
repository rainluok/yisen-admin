package com.yisen.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应状态码枚举
 *
 * @author yisen
 */
@Getter
@AllArgsConstructor
public enum ResponseCodeEnum {

    /**
     * 成功
     */
    SUCCESS(200, "操作成功"),

    /**
     * 失败
     */
    FAIL(500, "操作失败"),

    /**
     * 客户端错误 4xx
     */
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未授权，请先登录"),
    FORBIDDEN(403, "没有权限访问"),
    NOT_FOUND(404, "请求的资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不允许"),
    REQUEST_TIMEOUT(408, "请求超时"),
    CONFLICT(409, "数据冲突"),
    TOO_MANY_REQUESTS(429, "请求过于频繁，请稍后再试"),

    /**
     * 服务器错误 5xx
     */
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    BAD_GATEWAY(502, "网关错误"),
    SERVICE_UNAVAILABLE(503, "服务暂时不可用"),
    GATEWAY_TIMEOUT(504, "网关超时"),

    /**
     * 业务错误 1xxx
     */
    BUSINESS_ERROR(1000, "业务处理失败"),
    VALIDATION_ERROR(1001, "数据校验失败"),
    DUPLICATE_KEY_ERROR(1002, "数据重复"),
    DATA_NOT_FOUND(1003, "数据不存在"),
    DATA_ALREADY_EXISTS(1004, "数据已存在"),

    /**
     * 用户相关错误 2xxx
     */
    USER_NOT_FOUND(2001, "用户不存在"),
    USER_DISABLED(2002, "用户已被禁用"),
    USER_LOCKED(2003, "用户已被锁定"),
    USERNAME_PASSWORD_ERROR(2004, "用户名或密码错误"),
    USERNAME_EXISTS(2005, "用户名已存在"),
    USER_NOT_LOGIN(2006, "用户未登录"),
    TOKEN_INVALID(2007, "Token无效"),
    TOKEN_EXPIRED(2008, "Token已过期"),
    PERMISSION_DENIED(2009, "没有访问权限"),
    OLD_PASSWORD_ERROR(2010, "原密码错误"),

    /**
     * 文件相关错误 3xxx
     */
    FILE_UPLOAD_ERROR(3001, "文件上传失败"),
    FILE_SIZE_EXCEEDED(3002, "文件大小超出限制"),
    FILE_TYPE_NOT_ALLOWED(3003, "文件类型不允许"),
    FILE_NOT_FOUND(3004, "文件不存在"),
    FILE_DOWNLOAD_ERROR(3005, "文件下载失败"),

    /**
     * 数据库相关错误 4xxx
     */
    DATABASE_ERROR(4001, "数据库操作失败"),
    SQL_SYNTAX_ERROR(4002, "SQL语法错误"),
    CONSTRAINT_VIOLATION(4003, "违反约束条件"),

    /**
     * 第三方服务错误 5xxx
     */
    THIRD_PARTY_SERVICE_ERROR(5001, "第三方服务调用失败"),
    SMS_SEND_ERROR(5002, "短信发送失败"),
    EMAIL_SEND_ERROR(5003, "邮件发送失败");

    /**
     * 状态码
     */
    private  Integer code;

    /**
     * 消息
     */
    private  String message;

    ResponseCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }


    /**
     * 根据状态码获取枚举
     *
     * @param code 状态码
     * @return 响应码枚举
     */
    public static ResponseCodeEnum getByCode(Integer code) {
        for (ResponseCodeEnum responseCode : values()) {
            if (responseCode.getCode().equals(code)) {
                return responseCode;
            }
        }
        return FAIL;
    }

}

