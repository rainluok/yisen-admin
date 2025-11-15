package com.yisen.module.system.user.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 修改用户请求参数
 *
 * @author rainluo
 * @date 2025-11-14
 */
@Data
@Schema(description = "修改用户请求参数")
public class UserUpdateDTO {

    /**
     * 用户ID
     */
    @NotBlank(message = "用户ID不能为空")
    @Schema(description = "用户ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

    /**
     * 真实姓名
     */
    @Size(max = 50, message = "真实姓名长度不能超过50个字符")
    @Schema(description = "真实姓名", example = "张三")
    private String realName;

    /**
     * 性别 0-未知 1-男 2-女
     */
    @Schema(description = "性别", example = "1", allowableValues = {"0", "1", "2"})
    private Integer gender;

    /**
     * 头像
     */
    @Schema(description = "头像URL", example = "https://example.com/avatar.jpg")
    private String avatar;

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确")
    @Schema(description = "邮箱", example = "admin@example.com")
    private String email;

    /**
     * 部门ID
     */
    @NotBlank(message = "部门不能为空")
    @Schema(description = "部门ID", example = "1")
    private String departId;

    /**
     * 角色ID列表
     */
    @Schema(description = "角色ID列表", example = "[\"1\", \"2\"]")
    private List<String> roleIds;

}

