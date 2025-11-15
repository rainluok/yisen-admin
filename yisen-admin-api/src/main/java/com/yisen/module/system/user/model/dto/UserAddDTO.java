package com.yisen.module.system.user.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 新增用户请求参数
 *
 * @author rainluo
 * @date 2025-11-14
 */
@Data
@Schema(description = "新增用户请求参数")
public class UserAddDTO {

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 4, max = 20, message = "用户名长度为4-20个字符")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "用户名只能包含字母、数字和下划线")
    @Schema(description = "用户名", example = "admin", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度为6-20个字符")
    @Schema(description = "密码", example = "123456", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    /**
     * 真实姓名
     */
    @NotBlank(message = "真实姓名不能为空")
    @Size(max = 50, message = "真实姓名长度不能超过50个字符")
    @Schema(description = "真实姓名", example = "张三", requiredMode = Schema.RequiredMode.REQUIRED)
    private String realName;

    /**
     * 性别 0-未知 1-男 2-女
     */
    @Schema(description = "性别", example = "1", allowableValues = {"0", "1", "2"})
    private Integer gender = 0;

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

    /**
     * 用户状态 0-禁用 1-启用
     */
    @Schema(description = "用户状态", example = "1", allowableValues = {"0", "1"})
    private Integer status = 1;

}

