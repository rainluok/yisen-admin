package com.yisen.module.system.user.controller;

import com.yisen.common.annotation.OperationLog;
import com.yisen.common.annotation.RequirePermission;
import com.yisen.common.enums.BusinessType;
import com.yisen.common.enums.OperationType;
import com.yisen.common.model.PageRequest;
import com.yisen.common.model.PageResult;
import com.yisen.common.result.Result;
import com.yisen.module.system.user.model.dto.UserAddDTO;
import com.yisen.module.system.user.model.dto.UserQueryDTO;
import com.yisen.module.system.user.model.dto.UserResetPasswordDTO;
import com.yisen.module.system.user.model.dto.UserUpdateDTO;
import com.yisen.module.system.user.model.vo.UserDetailVO;
import com.yisen.module.system.user.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理接口
 *
 * @author rainluo
 * @date 2025-11-14
 */
@Slf4j
@RestController
@RequestMapping("/sys/user")
@RequiredArgsConstructor
@Tag(name = "用户管理", description = "用户相关接口")
public class SysUserController {

    private final SysUserService sysUserService;

    /**
     * 分页查询用户列表
     */
    @PostMapping("/page")
    @Operation(summary = "分页查询用户列表", description = "支持多条件查询、排序")
    @RequirePermission("sys:user:list")
    public Result<PageResult<UserDetailVO>> pageUsers(
            @RequestBody @Valid PageRequest<UserQueryDTO> pageRequest) {
        PageResult<UserDetailVO> result = sysUserService.pageUsers(pageRequest);
        return Result.success(result);
    }

    /**
     * 新增用户
     */
    @PostMapping("/add")
    @Operation(summary = "新增用户", description = "新增系统用户，并分配角色")
    @OperationLog(value = "新增用户", type = OperationType.INSERT, bizType = BusinessType.USER)
    @RequirePermission("sys:user:add")
    public Result<String> addUser(@RequestBody @Valid UserAddDTO dto) {
        String userId = sysUserService.addUser(dto);
        return Result.success("新增用户成功", userId);
    }

    /**
     * 获取用户详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取用户详情", description = "根据用户ID获取详细信息，包含角色列表")
    @RequirePermission("sys:user:detail")
    public Result<UserDetailVO> getUserDetail(
            @Parameter(description = "用户ID", required = true)
            @PathVariable String id) {
        UserDetailVO detail = sysUserService.getUserDetail(id);
        return Result.success(detail);
    }

    /**
     * 修改用户信息
     */
    @PutMapping("/update")
    @Operation(summary = "修改用户信息", description = "修改用户基本信息及角色关系")
    @OperationLog(value = "修改用户信息", type = OperationType.UPDATE, bizType = BusinessType.USER)
    @RequirePermission("sys:user:update")
    public Result<Void> updateUser(@RequestBody @Valid UserUpdateDTO dto) {
        sysUserService.updateUser(dto);
        return Result.success("修改用户成功");
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户", description = "逻辑删除用户，同时删除用户角色关系")
    @OperationLog(value = "删除用户", type = OperationType.DELETE, bizType = BusinessType.USER)
    @RequirePermission("sys:user:delete")
    public Result<Void> deleteUser(
            @Parameter(description = "用户ID", required = true)
            @PathVariable String id) {
        sysUserService.deleteUser(id);
        return Result.success("删除用户成功");
    }

    /**
     * 启用/禁用用户
     */
    @PutMapping("/status")
    @Operation(summary = "启用/禁用用户", description = "修改用户状态：0-禁用，1-启用")
    @OperationLog(value = "启用/禁用用户", type = OperationType.UPDATE, bizType = BusinessType.USER)
    @RequirePermission("sys:user:update")
    public Result<Void> updateUserStatus(
            @Parameter(description = "用户ID", required = true)
            @RequestParam String id,
            @Parameter(description = "状态：0-禁用，1-启用", required = true)
            @RequestParam Integer status) {
        sysUserService.updateUserStatus(id, status);
        return Result.success(status == 1 ? "启用用户成功" : "禁用用户成功");
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
