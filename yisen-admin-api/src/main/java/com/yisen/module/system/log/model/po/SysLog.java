package com.yisen.module.system.log.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 操作日志
 *
 * @TableName sys_log
 */
@TableName(value = "sys_log")
@Data
public class SysLog {
    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 操作内容
     */
    private String operation;

    /**
     * 操作类型 1-新增 2-修改 3-删除 4-查询
     */
    private Integer operationType;

    /**
     * 业务类型（如：user, order）
     */
    private String bizType;

    /**
     * 业务ID（关联具体数据）
     */
    private String bizId;

    /**
     * 请求URL
     */
    private String requestUrl;

    /**
     * 请求方式
     */
    private String requestMethod;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 状态 0-失败 1-成功
     */
    private Integer status;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 操作IP
     */
    private String ip;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 耗时(ms)
     */
    private Integer spendTime;

    /**
     * 操作时间
     */
    private LocalDateTime createTime;
}