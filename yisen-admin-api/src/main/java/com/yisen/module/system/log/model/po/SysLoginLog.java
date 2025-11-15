package com.yisen.module.system.log.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 登录日志
 *
 * @TableName sys_login_log
 */
@TableName(value = "sys_login_log")
@Data
public class SysLoginLog {
    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户名（登录账号）
     */
    private String username;

    /**
     * 状态：0-失败，1-成功
     */
    private Integer status;

    /**
     * 登录IP
     */
    private String ip;

    /**
     * IP归属地（可选）
     */
    private String ipLocation;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 描述信息（如：密码错误、验证码不正确）
     */
    private String msg;

    /**
     * 登录时间
     */
    private Date loginTime;
}