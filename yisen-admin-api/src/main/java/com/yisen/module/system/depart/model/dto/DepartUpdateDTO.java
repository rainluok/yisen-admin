package com.yisen.module.system.depart.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 修改部门请求参数
 *
 * @author rainluo
 * @date 2025-11-14
 */
@Data
@Schema(description = "修改部门请求参数")
public class DepartUpdateDTO {

    /**
     * 部门ID
     */
    @NotBlank(message = "部门ID不能为空")
    @Schema(description = "部门ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

    /**
     * 父部门ID
     */
    @Schema(description = "父部门ID", example = "0")
    private String parentId;

    /**
     * 部门名称
     */
    @Size(max = 50, message = "部门名称长度不能超过50个字符")
    @Schema(description = "部门名称", example = "技术部")
    private String departName;

    /**
     * 负责人
     */
    @Size(max = 50, message = "负责人长度不能超过50个字符")
    @Schema(description = "负责人", example = "张三")
    private String leader;

    /**
     * 部门电话
     */
    @Size(max = 20, message = "部门电话长度不能超过20个字符")
    @Schema(description = "部门电话", example = "010-12345678")
    private String phone;

    /**
     * 部门邮箱
     */
    @Size(max = 50, message = "部门邮箱长度不能超过50个字符")
    @Pattern(regexp = "^$|^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "邮箱格式不正确")
    @Schema(description = "部门邮箱", example = "tech@company.com")
    private String email;

    /**
     * 状态 0-禁用 1-启用
     */
    @Schema(description = "状态", example = "1", allowableValues = {"0", "1"})
    private Integer status;

    /**
     * 排序
     */
    @Schema(description = "排序", example = "1")
    private Integer sort;

}

