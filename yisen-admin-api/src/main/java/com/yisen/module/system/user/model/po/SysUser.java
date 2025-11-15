package com.yisen.module.system.user.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 系统用户表
 *
 * @TableName sys_user
 */
@TableName(value = "sys_user")
@Data
public class SysUser {
    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 性别 0-未知 1-男 2-女
     */
    private Integer gender;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 部门id
     */
    private String departId;

    /**
     * 最后登录IP
     */
    private String loginIp;

    /**
     * 最后登录时间
     */
    private Date loginTime;

    /**
     * 用户状态 0-禁用 1-启用
     */
    private Integer status;

    /**
     * 逻辑删除 0-未删除 1-已删除
     */
    private Integer isDeleted;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;
}