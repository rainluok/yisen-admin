package com.yisen.module.system.role.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 角色菜单关系表
 *
 * @author rainluo
 * @date 2025-11-14
 */
@TableName(value = "sys_role_menu")
@Data
public class SysRoleMenu {

    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 角色ID
     */
    private String roleId;

    /**
     * 菜单ID
     */
    private String menuId;

}

