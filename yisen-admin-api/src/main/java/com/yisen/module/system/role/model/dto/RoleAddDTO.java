package com.yisen.module.system.role.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 新增角色请求参数
 *
 * @author rainluo
 * @date 2025-11-14
 */
@Data
@Schema(description = "新增角色请求参数")
public class RoleAddDTO {

    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空")
    @Size(max = 50, message = "角色名称长度不能超过50个字符")
    @Schema(description = "角色名称", example = "管理员", requiredMode = Schema.RequiredMode.REQUIRED)
    private String roleName;

    /**
     * 角色编码
     */
    @NotBlank(message = "角色编码不能为空")
    @Size(max = 50, message = "角色编码长度不能超过50个字符")
    @Pattern(regexp = "^[A-Z_]+$", message = "角色编码只能包含大写字母和下划线")
    @Schema(description = "角色编码", example = "ROLE_ADMIN", requiredMode = Schema.RequiredMode.REQUIRED)
    private String roleCode;

    /**
     * 描述
     */
    @Size(max = 200, message = "描述长度不能超过200个字符")
    @Schema(description = "描述", example = "系统管理员角色")
    private String description;

    /**
     * 菜单ID列表
     */
    @Schema(description = "菜单ID列表", example = "[\"1\", \"2\", \"3\"]")
    private List<String> menuIds;

    /**
     * 状态 0-禁用 1-启用
     */
    @Schema(description = "状态", example = "1", allowableValues = {"0", "1"})
    private Integer status = 1;

}

