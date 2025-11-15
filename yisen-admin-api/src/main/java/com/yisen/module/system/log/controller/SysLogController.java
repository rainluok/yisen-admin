package com.yisen.module.system.log.controller;

import com.yisen.common.annotation.RequirePermission;
import com.yisen.common.model.PageRequest;
import com.yisen.common.model.PageResult;
import com.yisen.common.result.Result;
import com.yisen.module.system.log.model.dto.LogQueryDTO;
import com.yisen.module.system.log.model.vo.LogVO;
import com.yisen.module.system.log.service.SysLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 操作日志管理接口
 *
 * @author rainluo
 * @date 2025-11-15
 */
@Slf4j
@RestController
@RequestMapping("/sys/log")
@RequiredArgsConstructor
@Tag(name = "操作日志管理", description = "操作日志相关接口")
public class SysLogController {

    private final SysLogService sysLogService;

    /**
     * 分页查询操作日志列表
     */
    @PostMapping("/page")
    @Operation(summary = "分页查询操作日志列表", description = "支持多条件查询、排序")
    @RequirePermission("sys:log:list")
    public Result<PageResult<LogVO>> pageLogs(
            @RequestBody @Valid PageRequest<LogQueryDTO> pageRequest) {
        PageResult<LogVO> result = sysLogService.pageLogs(pageRequest);
        return Result.success(result);
    }

    /**
     * 获取操作日志详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取操作日志详情", description = "根据日志ID获取详细信息")
    @RequirePermission("sys:log:detail")
    public Result<LogVO> getLogDetail(
            @Parameter(description = "日志ID", required = true)
            @PathVariable String id) {
        LogVO detail = sysLogService.getLogDetail(id);
        return Result.success(detail);
    }

    /**
     * 删除操作日志
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除操作日志", description = "删除单条操作日志")
    @RequirePermission("sys:log:delete")
    public Result<Void> deleteLog(
            @Parameter(description = "日志ID", required = true)
            @PathVariable String id) {
        sysLogService.deleteLog(id);
        return Result.success("删除操作日志成功");
    }

    /**
     * 批量删除操作日志
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除操作日志", description = "批量删除操作日志")
    @RequirePermission("sys:log:delete")
    public Result<Void> deleteBatch(@RequestBody String[] ids) {
        sysLogService.deleteBatch(ids);
        return Result.success("批量删除操作日志成功");
    }

    /**
     * 清空操作日志
     */
    @DeleteMapping("/clear")
    @Operation(summary = "清空操作日志", description = "清空所有操作日志")
    @RequirePermission("sys:log:clear")
    public Result<Void> clearLogs() {
        sysLogService.clearLogs();
        return Result.success("清空操作日志成功");
    }

}

