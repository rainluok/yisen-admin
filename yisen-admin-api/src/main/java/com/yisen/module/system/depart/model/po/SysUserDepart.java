package com.yisen.module.system.depart.model.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户部门关联表
 *
 * @TableName sys_user_depart
 */
@TableName(value = "sys_user_depart")
@Data
public class SysUserDepart {
    /**
     * 主键id
     */
    @TableId
    private String id;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 部门ID
     */
    private String departId;
}