package com.yisen.module.system.role.controller;

import com.yisen.common.model.PageRequest;
import com.yisen.common.model.PageResult;
import com.yisen.common.result.Result;
import com.yisen.module.system.role.model.dto.RoleAddDTO;
import com.yisen.module.system.role.model.dto.RoleMenuAssignDTO;
import com.yisen.module.system.role.model.dto.RoleQueryDTO;
import com.yisen.module.system.role.model.dto.RoleUpdateDTO;
import com.yisen.module.system.role.model.vo.RoleDetailVO;
import com.yisen.module.system.role.service.SysRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 角色管理接口
 *
 * @author rainluo
 * @date 2025-11-14
 */
@Slf4j
@RestController
@RequestMapping("/sys/role")
@RequiredArgsConstructor
@Tag(name = "角色管理", description = "角色相关接口")
public class SysRoleController {

    private final SysRoleService sysRoleService;

    /**
     * 分页查询角色列表
     */
    @PostMapping("/page")
    @Operation(summary = "分页查询角色列表", description = "支持多条件查询、排序")
    public Result<PageResult<RoleDetailVO>> pageRoles(
            @RequestBody @Valid PageRequest<RoleQueryDTO> pageRequest) {
        PageResult<RoleDetailVO> result = sysRoleService.pageRoles(pageRequest);
        return Result.success(result);
    }

    /**
     * 新增角色
     */
    @PostMapping("/add")
    @Operation(summary = "新增角色", description = "新增系统角色，并分配菜单权限")
    public Result<String> addRole(@RequestBody @Valid RoleAddDTO dto) {
        String roleId = sysRoleService.addRole(dto);
        return Result.success("新增角色成功", roleId);
    }

    /**
     * 获取角色详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取角色详情", description = "根据角色ID获取详细信息，包含关联菜单/权限")
    public Result<RoleDetailVO> getRoleDetail(
            @Parameter(description = "角色ID", required = true)
            @PathVariable String id) {
        RoleDetailVO detail = sysRoleService.getRoleDetail(id);
        return Result.success(detail);
    }

    /**
     * 修改角色基本信息
     */
    @PutMapping("/update")
    @Operation(summary = "修改角色基本信息", description = "修改角色名称、描述、状态等基本信息")
    public Result<Void> updateRole(@RequestBody @Valid RoleUpdateDTO dto) {
        sysRoleService.updateRole(dto);
        return Result.success("修改角色成功");
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除角色", description = "逻辑删除角色，同时删除角色菜单关系")
    public Result<Void> deleteRole(
            @Parameter(description = "角色ID", required = true)
            @PathVariable String id) {
        sysRoleService.deleteRole(id);
        return Result.success("删除角色成功");
    }

    /**
     * 分配角色菜单权限
     */
    @PostMapping("/assign-menus")
    @Operation(summary = "分配角色菜单权限", description = "给角色分配菜单权限（接收菜单ID列表）")
    public Result<Void> assignMenus(@RequestBody @Valid RoleMenuAssignDTO dto) {
        sysRoleService.assignMenus(dto);
        return Result.success("分配角色菜单权限成功");
    }

}

