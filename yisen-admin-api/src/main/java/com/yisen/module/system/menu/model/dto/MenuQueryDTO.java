package com.yisen.module.system.menu.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 菜单查询参数
 *
 * @author rainluo
 * @date 2025-11-14
 */
@Data
@Schema(description = "菜单查询参数")
public class MenuQueryDTO {

    /**
     * 菜单名称（模糊查询）
     */
    @Schema(description = "菜单名称", example = "用户管理")
    private String menuName;

    /**
     * 菜单编码（模糊查询）
     */
    @Schema(description = "菜单编码", example = "user:manage")
    private String menuCode;

    /**
     * 类型 1-菜单 2-按钮
     */
    @Schema(description = "类型", example = "1", allowableValues = {"1", "2"})
    private Integer type;

    /**
     * 状态 0-禁用 1-启用
     */
    @Schema(description = "状态", example = "1", allowableValues = {"0", "1"})
    private Integer status;

}

