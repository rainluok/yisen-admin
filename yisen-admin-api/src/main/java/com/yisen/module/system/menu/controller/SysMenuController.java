package com.yisen.module.system.menu.controller;

import com.yisen.common.result.Result;
import com.yisen.module.system.menu.model.dto.MenuAddDTO;
import com.yisen.module.system.menu.model.dto.MenuQueryDTO;
import com.yisen.module.system.menu.model.dto.MenuUpdateDTO;
import com.yisen.module.system.menu.model.vo.MenuTreeVO;
import com.yisen.module.system.menu.service.SysMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单管理接口
 *
 * @author rainluo
 * @date 2025-11-14
 */
@Slf4j
@RestController
@RequestMapping("/sys/menu")
@RequiredArgsConstructor
@Tag(name = "菜单管理", description = "菜单相关接口")
public class SysMenuController {

    private final SysMenuService sysMenuService;

    /**
     * 查询所有菜单（平铺列表）
     */
    @PostMapping("/list")
    @Operation(summary = "查询所有菜单", description = "查询所有菜单的平铺列表，用于管理")
    public Result<List<MenuTreeVO>> getAllMenus(@RequestBody(required = false) MenuQueryDTO queryDTO) {
        List<MenuTreeVO> list = sysMenuService.getAllMenus(queryDTO);
        return Result.success(list);
    }

    /**
     * 获取菜单树形结构
     */
    @GetMapping("/tree")
    @Operation(summary = "获取菜单树", description = "获取菜单树形结构，用于前端渲染侧边栏")
    public Result<List<MenuTreeVO>> getMenuTree() {
        List<MenuTreeVO> tree = sysMenuService.getMenuTree();
        return Result.success(tree);
    }

    /**
     * 获取用户可用菜单树
     */
    @GetMapping("/user-tree/{userId}")
    @Operation(summary = "获取用户菜单树", description = "获取当前用户可用菜单树，带权限过滤")
    public Result<List<MenuTreeVO>> getUserMenuTree(
            @Parameter(description = "用户ID", required = true)
            @PathVariable String userId) {
        List<MenuTreeVO> tree = sysMenuService.getUserMenuTree(userId);
        return Result.success(tree);
    }

    /**
     * 新增菜单
     */
    @PostMapping("/add")
    @Operation(summary = "新增菜单", description = "新增系统菜单")
    public Result<String> addMenu(@RequestBody @Valid MenuAddDTO dto) {
        String menuId = sysMenuService.addMenu(dto);
        return Result.success("新增菜单成功", menuId);
    }

    /**
     * 获取菜单详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取菜单详情", description = "根据菜单ID获取详细信息")
    public Result<MenuTreeVO> getMenuDetail(
            @Parameter(description = "菜单ID", required = true)
            @PathVariable String id) {
        MenuTreeVO detail = sysMenuService.getMenuDetail(id);
        return Result.success(detail);
    }

    /**
     * 修改菜单
     */
    @PutMapping("/update")
    @Operation(summary = "修改菜单", description = "修改菜单信息")
    public Result<Void> updateMenu(@RequestBody @Valid MenuUpdateDTO dto) {
        sysMenuService.updateMenu(dto);
        return Result.success("修改菜单成功");
    }

    /**
     * 删除菜单
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除菜单", description = "逻辑删除菜单")
    public Result<Void> deleteMenu(
            @Parameter(description = "菜单ID", required = true)
            @PathVariable String id) {
        sysMenuService.deleteMenu(id);
        return Result.success("删除菜单成功");
    }

}
