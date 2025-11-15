package com.yisen.module.system.depart.controller;

import com.yisen.common.annotation.OperationLog;
import com.yisen.common.annotation.RequirePermission;
import com.yisen.common.enums.BusinessType;
import com.yisen.common.enums.OperationType;
import com.yisen.common.model.PageRequest;
import com.yisen.common.model.PageResult;
import com.yisen.common.result.Result;
import com.yisen.module.system.depart.model.dto.DepartAddDTO;
import com.yisen.module.system.depart.model.dto.DepartQueryDTO;
import com.yisen.module.system.depart.model.dto.DepartUpdateDTO;
import com.yisen.module.system.depart.model.vo.DepartTreeVO;
import com.yisen.module.system.depart.service.SysDepartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门管理接口
 *
 * @author rainluo
 * @date 2025-11-15
 */
@Slf4j
@RestController
@RequestMapping("/sys/depart")
@RequiredArgsConstructor
@Tag(name = "部门管理", description = "部门相关接口")
public class SysDepartController {

    private final SysDepartService sysDepartService;

    /**
     * 分页查询部门列表
     */
    @PostMapping("/page")
    @Operation(summary = "分页查询部门列表", description = "支持多条件查询、排序")
    @RequirePermission("sys:depart:list")
    public Result<PageResult<DepartTreeVO>> pageDeparts(
            @RequestBody @Valid PageRequest<DepartQueryDTO> pageRequest) {
        PageResult<DepartTreeVO> result = sysDepartService.pageDeparts(pageRequest);
        return Result.success(result);
    }

    /**
     * 查询所有部门（平铺列表）
     */
    @PostMapping("/list")
    @Operation(summary = "查询所有部门", description = "查询所有部门的平铺列表，用于管理")
    @RequirePermission("sys:depart:list")
    public Result<List<DepartTreeVO>> getAllDeparts(@RequestBody(required = false) DepartQueryDTO queryDTO) {
        List<DepartTreeVO> list = sysDepartService.getAllDeparts(queryDTO);
        return Result.success(list);
    }

    /**
     * 获取部门树形结构
     */
    @GetMapping("/tree")
    @Operation(summary = "获取部门树", description = "获取部门树形结构，用于前端渲染")
    public Result<List<DepartTreeVO>> getDepartTree() {
        List<DepartTreeVO> tree = sysDepartService.getDepartTree();
        return Result.success(tree);
    }

    /**
     * 新增部门
     */
    @PostMapping("/add")
    @Operation(summary = "新增部门", description = "新增系统部门")
    @OperationLog(value = "新增部门", type = OperationType.INSERT, bizType = BusinessType.DEPART)
    @RequirePermission("sys:depart:add")
    public Result<String> addDepart(@RequestBody @Valid DepartAddDTO dto) {
        String departId = sysDepartService.addDepart(dto);
        return Result.success("新增部门成功", departId);
    }

    /**
     * 获取部门详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取部门详情", description = "根据部门ID获取详细信息")
    @RequirePermission("sys:depart:detail")
    public Result<DepartTreeVO> getDepartDetail(
            @Parameter(description = "部门ID", required = true)
            @PathVariable String id) {
        DepartTreeVO detail = sysDepartService.getDepartDetail(id);
        return Result.success(detail);
    }

    /**
     * 修改部门信息
     */
    @PutMapping("/update")
    @Operation(summary = "修改部门信息", description = "修改部门基本信息")
    @OperationLog(value = "修改部门信息", type = OperationType.UPDATE, bizType = BusinessType.DEPART)
    @RequirePermission("sys:depart:update")
    public Result<Void> updateDepart(@RequestBody @Valid DepartUpdateDTO dto) {
        sysDepartService.updateDepart(dto);
        return Result.success("修改部门成功");
    }

    /**
     * 删除部门
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除部门", description = "逻辑删除部门")
    @OperationLog(value = "删除部门", type = OperationType.DELETE, bizType = BusinessType.DEPART)
    @RequirePermission("sys:depart:delete")
    public Result<Void> deleteDepart(
            @Parameter(description = "部门ID", required = true)
            @PathVariable String id) {
        sysDepartService.deleteDepart(id);
        return Result.success("删除部门成功");
    }

    /**
     * 启用/禁用部门
     */
    @PutMapping("/status")
    @Operation(summary = "启用/禁用部门", description = "修改部门状态：0-禁用，1-启用")
    @OperationLog(value = "启用/禁用部门", type = OperationType.UPDATE, bizType = BusinessType.DEPART)
    @RequirePermission("sys:depart:update")
    public Result<Void> updateDepartStatus(
            @Parameter(description = "部门ID", required = true)
            @RequestParam String id,
            @Parameter(description = "状态：0-禁用，1-启用", required = true)
            @RequestParam Integer status) {
        sysDepartService.updateDepartStatus(id, status);
        return Result.success(status == 1 ? "启用部门成功" : "禁用部门成功");
    }

}

