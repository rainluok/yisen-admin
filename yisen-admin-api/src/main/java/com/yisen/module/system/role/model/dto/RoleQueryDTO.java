package com.yisen.module.system.role.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 角色查询参数
 *
 * @author rainluo
 * @date 2025-11-14
 */
@Data
@Schema(description = "角色查询参数")
public class RoleQueryDTO {

    /**
     * 角色名称（模糊查询）
     */
    @Schema(description = "角色名称", example = "管理员")
    private String roleName;

    /**
     * 角色编码（模糊查询）
     */
    @Schema(description = "角色编码", example = "ROLE_ADMIN")
    private String roleCode;

    /**
     * 状态 0-禁用 1-启用
     */
    @Schema(description = "状态", example = "1", allowableValues = {"0", "1"})
    private Integer status;

    /**
     * 创建时间开始
     */
    @Schema(description = "创建时间开始", example = "2025-01-01 00:00:00")
    private String createTimeStart;

    /**
     * 创建时间结束
     */
    @Schema(description = "创建时间结束", example = "2025-12-31 23:59:59")
    private String createTimeEnd;

}

