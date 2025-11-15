package com.yisen.module.system.user.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户角色关系表
 *
 * @TableName sys_user_role
 */
@TableName(value = "sys_user_role")
@Data
public class SysUserRole {
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
     * 角色ID
     */
    private String roleId;
}