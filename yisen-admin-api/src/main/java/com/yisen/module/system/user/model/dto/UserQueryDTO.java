package com.yisen.module.system.user.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户查询参数
 *
 * @author rainluo
 * @date 2025-11-14
 */
@Data
@Schema(description = "用户查询参数")
public class UserQueryDTO {

    /**
     * 用户名（模糊查询）
     */
    @Schema(description = "用户名", example = "admin")
    private String username;

    /**
     * 真实姓名（模糊查询）
     */
    @Schema(description = "真实姓名", example = "张三")
    private String realName;

    /**
     * 性别 0-未知 1-男 2-女
     */
    @Schema(description = "性别", example = "1", allowableValues = {"0", "1", "2"})
    private Integer gender;

    /**
     * 邮箱（模糊查询）
     */
    @Schema(description = "邮箱", example = "admin@example.com")
    private String email;

    /**
     * 部门ID
     */
    @Schema(description = "部门ID", example = "1")
    private String departId;

    /**
     * 用户状态 0-禁用 1-启用
     */
    @Schema(description = "用户状态", example = "1", allowableValues = {"0", "1"})
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

