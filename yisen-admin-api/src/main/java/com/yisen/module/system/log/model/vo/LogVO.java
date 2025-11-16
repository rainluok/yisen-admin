package com.yisen.module.system.log.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 操作日志VO
 *
 * @author rainluo
 * @date 2025-11-15
 */
@Data
@Schema(description = "操作日志VO")
public class LogVO {

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
     * 用户名
     */
    @Schema(description = "用户名")
    private String username;

    /**
     * 操作内容
     */
    @Schema(description = "操作内容")
    private String operation;

    /**
     * 操作类型 1-新增 2-修改 3-删除 4-查询
     */
    @Schema(description = "操作类型")
    private Integer operationType;

    /**
     * 业务类型（如：user, order）
     */
    @Schema(description = "业务类型")
    private String bizType;

    /**
     * 业务ID（关联具体数据）
     */
    @Schema(description = "业务ID")
    private String bizId;

    /**
     * 请求URL
     */
    @Schema(description = "请求URL")
    private String requestUrl;

    /**
     * 请求方式
     */
    @Schema(description = "请求方式")
    private String requestMethod;

    /**
     * 请求参数
     */
    @Schema(description = "请求参数")
    private String requestParams;

    /**
     * 状态 0-失败 1-成功
     */
    @Schema(description = "状态")
    private Integer status;

    /**
     * 错误信息
     */
    @Schema(description = "错误信息")
    private String errorMessage;

    /**
     * 操作IP
     */
    @Schema(description = "操作IP")
    private String ip;

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
     * 耗时(ms)
     */
    @Schema(description = "耗时(ms)")
    private Integer spendTime;

    /**
     * 操作时间
     */
    @Schema(description = "操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

}

