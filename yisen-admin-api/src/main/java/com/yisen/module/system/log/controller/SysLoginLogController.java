package com.yisen.module.system.log.controller;

import com.yisen.common.annotation.RequirePermission;
import com.yisen.common.model.PageRequest;
import com.yisen.common.model.PageResult;
import com.yisen.common.result.Result;
import com.yisen.module.system.log.model.dto.LoginLogQueryDTO;
import com.yisen.module.system.log.model.vo.LoginLogVO;
import com.yisen.module.system.log.service.SysLoginLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 登录日志管理接口
 *
 * @author rainluo
 * @date 2025-11-15
 */
@Slf4j
@RestController
@RequestMapping("/sys/login-log")
@RequiredArgsConstructor
@Tag(name = "登录日志管理", description = "登录日志相关接口")
public class SysLoginLogController {

    private final SysLoginLogService sysLoginLogService;

    /**
     * 分页查询登录日志列表
     */
    @PostMapping("/page")
    @Operation(summary = "分页查询登录日志列表", description = "支持多条件查询、排序")
    @RequirePermission("sys:login-log:list")
    public Result<PageResult<LoginLogVO>> pageLoginLogs(
            @RequestBody @Valid PageRequest<LoginLogQueryDTO> pageRequest) {
        PageResult<LoginLogVO> result = sysLoginLogService.pageLoginLogs(pageRequest);
        return Result.success(result);
    }

    /**
     * 获取登录日志详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取登录日志详情", description = "根据日志ID获取详细信息")
    @RequirePermission("sys:login-log:detail")
    public Result<LoginLogVO> getLoginLogDetail(
            @Parameter(description = "日志ID", required = true)
            @PathVariable String id) {
        LoginLogVO detail = sysLoginLogService.getLoginLogDetail(id);
        return Result.success(detail);
    }

    /**
     * 删除登录日志
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除登录日志", description = "删除单条登录日志")
    @RequirePermission("sys:login-log:delete")
    public Result<Void> deleteLoginLog(
            @Parameter(description = "日志ID", required = true)
            @PathVariable String id) {
        sysLoginLogService.deleteLoginLog(id);
        return Result.success("删除登录日志成功");
    }

    /**
     * 批量删除登录日志
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除登录日志", description = "批量删除登录日志")
    @RequirePermission("sys:login-log:delete")
    public Result<Void> deleteBatch(@RequestBody String[] ids) {
        sysLoginLogService.deleteBatch(ids);
        return Result.success("批量删除登录日志成功");
    }

    /**
     * 清空登录日志
     */
    @DeleteMapping("/clear")
    @Operation(summary = "清空登录日志", description = "清空所有登录日志")
    @RequirePermission("sys:login-log:clear")
    public Result<Void> clearLoginLogs() {
        sysLoginLogService.clearLoginLogs();
        return Result.success("清空登录日志成功");
    }

}

