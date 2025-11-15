package com.yisen.module.system.menu.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 新增菜单请求参数
 *
 * @author rainluo
 * @date 2025-11-14
 */
@Data
@Schema(description = "新增菜单请求参数")
public class MenuAddDTO {

    /**
     * 父菜单ID（顶级菜单为0）
     */
    @NotBlank(message = "父菜单ID不能为空")
    @Schema(description = "父菜单ID", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    private String parentId;

    /**
     * 菜单名称
     */
    @NotBlank(message = "菜单名称不能为空")
    @Size(max = 50, message = "菜单名称长度不能超过50个字符")
    @Schema(description = "菜单名称", example = "用户管理", requiredMode = Schema.RequiredMode.REQUIRED)
    private String menuName;

    /**
     * 菜单编码
     */
    @NotBlank(message = "菜单编码不能为空")
    @Size(max = 50, message = "菜单编码长度不能超过50个字符")
    @Schema(description = "菜单编码", example = "user:manage", requiredMode = Schema.RequiredMode.REQUIRED)
    private String menuCode;

    /**
     * 类型 1-菜单 2-按钮
     */
    @NotNull(message = "类型不能为空")
    @Schema(description = "类型", example = "1", allowableValues = {"1", "2"}, requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer type;

    /**
     * 路由路径
     */
    @Size(max = 200, message = "路由路径长度不能超过200个字符")
    @Schema(description = "路由路径", example = "/user")
    private String path;

    /**
     * 组件路径
     */
    @Size(max = 200, message = "组件路径长度不能超过200个字符")
    @Schema(description = "组件路径", example = "views/user/index")
    private String component;

    /**
     * 权限标识
     */
    @Size(max = 100, message = "权限标识长度不能超过100个字符")
    @Schema(description = "权限标识", example = "user:list")
    private String permission;

    /**
     * 图标
     */
    @Size(max = 100, message = "图标长度不能超过100个字符")
    @Schema(description = "图标", example = "el-icon-user")
    private String icon;

    /**
     * 是否隐藏 0-显示 1-隐藏
     */
    @Schema(description = "是否隐藏", example = "0", allowableValues = {"0", "1"})
    private Integer hidden = 0;

    /**
     * 布局类型 default-默认 blank-空白
     */
    @Size(max = 20, message = "布局类型长度不能超过20个字符")
    @Schema(description = "布局类型", example = "default")
    private String layout = "default";

    /**
     * 排序
     */
    @Schema(description = "排序", example = "1")
    private Integer sort = 0;

    /**
     * 状态 0-禁用 1-启用
     */
    @Schema(description = "状态", example = "1", allowableValues = {"0", "1"})
    private Integer status = 1;

}

