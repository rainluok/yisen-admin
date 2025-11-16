package com.yisen.module.system.log.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 登录日志VO
 *
 * @author rainluo
 * @date 2025-11-15
 */
@Data
@Schema(description = "登录日志VO")
public class LoginLogVO {

    /**
     * 日志ID
     */
    @Schema(description = "日志ID")
    private String id;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    private String userId;

    /**
     * 用户名（登录账号）
     */
    @Schema(description = "用户名")
    private String username;

    /**
     * 状态：0-失败，1-成功
     */
    @Schema(description = "状态")
    private Integer status;

    /**
     * 登录IP
     */
    @Schema(description = "登录IP")
    private String ip;

    /**
     * IP归属地（可选）
     */
    @Schema(description = "IP归属地")
    private String ipLocation;

    /**
     * 操作系统
     */
    @Schema(description = "操作系统")
    private String os;

    /**
     * 浏览器
     */
    @Schema(description = "浏览器")
    private String browser;

    /**
     * 描述信息（如：密码错误、验证码不正确）
     */
    @Schema(description = "描述信息")
    private String msg;

    /**
     * 登录时间
     */
    @Schema(description = "登录时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime loginTime;

}

