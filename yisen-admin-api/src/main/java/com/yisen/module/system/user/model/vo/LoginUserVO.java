package com.yisen.module.system.user.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

/**
 * @author rainluo
 * @version 1.0
 * @description
 * @date 2025/11/14 10:47
 */
@Data
public class LoginUserVO {

    @Schema(description = "Token")
    private String token;

    @Schema(description = "主键id")
    private String id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "真实姓名")
    private String realName;

    @Schema(description = "性别 0-未知 1-男 2-女")
    private Integer gender;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "部门id")
    private String departId;

    @Schema(description = "最后登录IP")
    private String loginIp;

    @Schema(description = "最后登录时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDate loginTime;

    @Schema(description = "用户状态 0-禁用 1-启用")
    private Integer status;

    @Schema(description = "角色")
    private Set<String> roles;

    @Schema(description = "权限")
    private Set<String> permissions;

    public LoginUserVO(){}
}
