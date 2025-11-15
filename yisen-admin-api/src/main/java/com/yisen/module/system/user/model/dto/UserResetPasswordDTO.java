package com.yisen.module.system.user.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 重置用户密码请求参数
 *
 * @author rainluo
 * @date 2025-11-14
 */
@Data
@Schema(description = "重置用户密码请求参数")
public class UserResetPasswordDTO {

    /**
     * 用户ID
     */
    @NotBlank(message = "用户ID不能为空")
    @Schema(description = "用户ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

    /**
     * 新密码
     */
    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度为6-20个字符")
    @Schema(description = "新密码", example = "123456", requiredMode = Schema.RequiredMode.REQUIRED)
    private String newPassword;

}

