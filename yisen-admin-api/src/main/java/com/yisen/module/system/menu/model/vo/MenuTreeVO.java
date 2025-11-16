package com.yisen.module.system.menu.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 菜单树形结构 VO
 *
 * @author rainluo
 * @date 2025-11-14
 */
@Data
@Schema(description = "菜单树形结构")
public class MenuTreeVO {

    /**
     * 菜单ID
     */
    @Schema(description = "菜单ID", example = "1")
    private String id;

    /**
     * 父菜单ID
     */
    @Schema(description = "父菜单ID", example = "0")
    private String parentId;

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

    /**
     * 路由路径
     */
    @Schema(description = "路由路径", example = "/user")
    private String path;

    /**
     * 组件路径
     */
    @Schema(description = "组件路径", example = "views/user/index")
    private String component;

    /**
     * 权限标识
     */
    @Schema(description = "权限标识", example = "user:list")
    private String permission;

    /**
     * 图标
     */
    @Schema(description = "图标", example = "el-icon-user")
    private String icon;

    /**
     * 是否隐藏 0-显示 1-隐藏
     */
    @Schema(description = "是否隐藏", example = "0")
    private Integer hidden;

    /**
     * 布局类型 default-默认 blank-空白
     */
    @Schema(description = "布局类型", example = "default")
    private String layout;

    /**
     * 排序
     */
    @Schema(description = "排序", example = "1")
    private Integer sort;

    /**
     * 状态 0-禁用 1-启用
     */
    @Schema(description = "状态", example = "1")
    private Integer status;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间", example = "2025-11-13 10:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 子菜单列表
     */
    @Schema(description = "子菜单列表")
    private List<MenuTreeVO> children;

}

