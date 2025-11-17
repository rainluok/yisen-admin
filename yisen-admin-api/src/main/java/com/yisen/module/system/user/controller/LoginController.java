package com.yisen.module.system.user.controller;

import com.yisen.common.annotation.OperationLog;
import com.yisen.common.annotation.RequirePermission;
import com.yisen.common.enums.BusinessType;
import com.yisen.common.enums.OperationType;
import com.yisen.common.result.Result;
import com.yisen.core.context.LoginUserContext;
import com.yisen.module.system.user.model.dto.LoginDTO;
import com.yisen.module.system.user.model.dto.UserResetPasswordDTO;
import com.yisen.module.system.user.model.vo.LoginUserVO;
import com.yisen.module.system.user.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 登录管理控制器
 *
 * @author rainluo
 * @version 1.0
 * @description 用户登录、登出、获取用户信息等接口
 * @date 2025/11/14 15:14
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "登录管理", description = "登录相关接口")
public class LoginController {

    private final SysUserService sysUserService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户登录，返回token和用户信息")
    public Result<LoginUserVO> login(@RequestBody @Valid LoginDTO dto) {
        LoginUserVO loginInfo = sysUserService.login(dto);
        return Result.success("登录成功", loginInfo);
    }

    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/userInfo")
    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户的详细信息")
    public Result<LoginUserVO> getUserInfo() {
        LoginUserVO userInfo = LoginUserContext.getUser();
        return Result.success(userInfo);
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    @Operation(summary = "用户登出", description = "用户登出，清除登录状态")
    public Result<Void> logout() {
        sysUserService.logout();
        return Result.success("登出成功");
    }

    /**
     * 重置用户密码
     */
    @PutMapping("/reset-password")
    @Operation(summary = "重置用户密码", description = "管理员重置用户密码")
    @OperationLog(value = "重置用户密码", type = OperationType.UPDATE, bizType = BusinessType.USER)
    @RequirePermission("sys:user:reset-password")
    public Result<Void> resetPassword(@RequestBody @Valid UserResetPasswordDTO dto) {
        sysUserService.resetPassword(dto);
        return Result.success("重置密码成功");
    }
}
