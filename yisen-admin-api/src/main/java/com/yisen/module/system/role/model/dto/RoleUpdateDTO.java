package com.yisen.module.system.role.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 修改角色请求参数
 *
 * @author rainluo
 * @date 2025-11-14
 */
@Data
@Schema(description = "修改角色请求参数")
public class RoleUpdateDTO {

    /**
     * 角色ID
     */
    @NotBlank(message = "角色ID不能为空")
    @Schema(description = "角色ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

    /**
     * 角色名称
     */
    @Size(max = 50, message = "角色名称长度不能超过50个字符")
    @Schema(description = "角色名称", example = "管理员")
    private String roleName;

    /**
     * 描述
     */
    @Size(max = 200, message = "描述长度不能超过200个字符")
    @Schema(description = "描述", example = "系统管理员角色")
    private String description;

    /**
     * 状态 0-禁用 1-启用
     */
    @Schema(description = "状态", example = "1", allowableValues = {"0", "1"})
    private Integer status;

}

