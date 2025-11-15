package com.yisen.module.system.role.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 角色详情 VO
 *
 * @author rainluo
 * @date 2025-11-14
 */
@Data
@Schema(description = "角色详情")
public class RoleDetailVO {

    /**
     * 角色ID
     */
    @Schema(description = "角色ID", example = "1")
    private String id;

    /**
     * 角色名称
     */
    @Schema(description = "角色名称", example = "管理员")
    private String roleName;

    /**
     * 角色编码
     */
    @Schema(description = "角色编码", example = "ROLE_ADMIN")
    private String roleCode;

    /**
     * 描述
     */
    @Schema(description = "描述", example = "系统管理员角色")
    private String description;

    /**
     * 状态 0-禁用 1-启用
     */
    @Schema(description = "状态", example = "1")
    private Integer status;

    /**
     * 菜单列表
     */
    @Schema(description = "菜单列表")
    private List<MenuVO> menus;

    /**
     * 创建人
     */
    @Schema(description = "创建人", example = "admin")
    private String createBy;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间", example = "2025-11-13 10:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新人
     */
    @Schema(description = "更新人", example = "admin")
    private String updateBy;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间", example = "2025-11-14 09:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 菜单信息
     */
    @Data
    @Schema(description = "菜单信息")
    public static class MenuVO {

        /**
         * 菜单ID
         */
        @Schema(description = "菜单ID", example = "1")
        private String id;

        /**
         * 菜单名称
         */
        @Schema(description = "菜单名称", example = "用户管理")
        private String menuName;

        /**
         * 菜单编码
         */
        @Schema(description = "菜单编码", example = "user:manage")
        private String menuCode;

        /**
         * 类型 1-菜单 2-按钮
         */
        @Schema(description = "类型", example = "1")
        private Integer type;

    }

}

