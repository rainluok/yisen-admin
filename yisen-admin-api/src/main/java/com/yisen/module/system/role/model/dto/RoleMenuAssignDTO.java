package com.yisen.module.system.role.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 分配角色菜单权限请求参数
 *
 * @author rainluo
 * @date 2025-11-14
 */
@Data
@Schema(description = "分配角色菜单权限请求参数")
public class RoleMenuAssignDTO {

    /**
     * 角色ID
     */
    @NotBlank(message = "角色ID不能为空")
    @Schema(description = "角色ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private String roleId;

    /**
     * 菜单ID列表
     */
    @NotEmpty(message = "菜单ID列表不能为空")
    @Schema(description = "菜单ID列表", example = "[\"1\", \"2\", \"3\"]", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<String> menuIds;

}

