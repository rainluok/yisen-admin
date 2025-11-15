package com.yisen.module.system.depart.model.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 系统部门表
 *
 * @TableName sys_depart
 */
@TableName(value = "sys_depart")
@Data
public class SysDepart {
    /**
     * 主键id
     */
    @TableId
    private String id;

    /**
     * 父部门ID
     */
    private String parentId;

    /**
     * 部门名称
     */
    private String departName;

    /**
     * 部门编码
     */
    private String departCode;

    /**
     * 负责人
     */
    private String leader;

    /**
     * 部门电话
     */
    private String phone;

    /**
     * 部门邮箱
     */
    private String email;

    /**
     * 状态 0-禁用 1-启用
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sort;

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