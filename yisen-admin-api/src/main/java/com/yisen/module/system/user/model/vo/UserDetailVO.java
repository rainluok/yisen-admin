package com.yisen.module.system.user.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 用户详情 VO
 *
 * @author rainluo
 * @date 2025-11-14
 */
@Data
@Schema(description = "用户详情")
public class UserDetailVO {

    /**
     * 用户ID
     */
    @Schema(description = "用户ID", example = "1")
    private String id;

    /**
     * 用户名
     */
    @Schema(description = "用户名", example = "admin")
    private String username;

    /**
     * 真实姓名
     */
    @Schema(description = "真实姓名", example = "张三")
    private String realName;

    /**
     * 性别 0-未知 1-男 2-女
     */
    @Schema(description = "性别", example = "1")
    private Integer gender;

    /**
     * 头像
     */
    @Schema(description = "头像URL", example = "https://example.com/avatar.jpg")
    private String avatar;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱", example = "admin@example.com")
    private String email;

    /**
     * 部门ID
     */
    @Schema(description = "部门ID", example = "1")
    private String departId;

    /**
     * 最后登录IP
     */
    @Schema(description = "最后登录IP", example = "127.0.0.1")
    private String loginIp;

    /**
     * 最后登录时间
     */
    @Schema(description = "最后登录时间", example = "2025-11-14 09:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime loginTime;

    /**
     * 用户状态 0-禁用 1-启用
     */
    @Schema(description = "用户状态", example = "1")
    private Integer status;

    /**
     * 角色列表
     */
    @Schema(description = "角色列表")
    private List<RoleVO> roles;

    /**
     * 创建人
     */
    @Schema(description = "创建人", example = "admin")
    private String createBy;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间", example = "2025-11-13 10:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @Schema(description = "更新人", example = "admin")
    private String updateBy;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间", example = "2025-11-14 09:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    /**
     * 角色信息
     */
    @Data
    @Schema(description = "角色信息")
    public static class RoleVO {

        /**
         * 角色ID
         */
        @Schema(description = "角色ID", example = "1")
        private String id;

        /**
         * 角色名称
         */
        @Schema(description = "角色名称", example = "管理员")
        private String roleName;

        /**
         * 角色编码
         */
        @Schema(description = "角色编码", example = "ROLE_ADMIN")
        private String roleCode;

    }

}

