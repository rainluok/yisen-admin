package com.yisen.module.system.user.controller;

import com.yisen.common.result.Result;
import com.yisen.common.util.UserUtil;
import com.yisen.module.system.user.model.dto.LoginDTO;
import com.yisen.module.system.user.model.vo.LoginInfoVO;
import com.yisen.module.system.user.model.vo.UserInfoVO;
import com.yisen.module.system.user.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    private final UserUtil userUtil;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户登录，返回token和用户信息")
    public Result<LoginInfoVO> login(@RequestBody @Valid LoginDTO dto) {
        LoginInfoVO loginInfo = sysUserService.login(dto);
        return Result.success("登录成功", loginInfo);
    }

    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/user/info")
    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户的详细信息")
    public Result<UserInfoVO> getUserInfo() {
        UserInfoVO userInfo = userUtil.getCurrentUser();
        return Result.success(userInfo);
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    @Operation(summary = "用户登出", description = "用户登出，清除登录状态")
    public Result<Void> logout() {
        log.info("用户登出: {}", userUtil.getCurrentUser().getUsername());
        // TODO: 可以在这里添加清除Redis缓存等逻辑
        return Result.success("登出成功");
    }
}
