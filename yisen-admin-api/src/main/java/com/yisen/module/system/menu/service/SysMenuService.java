package com.yisen.module.system.menu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yisen.module.system.menu.model.dto.MenuAddDTO;
import com.yisen.module.system.menu.model.dto.MenuQueryDTO;
import com.yisen.module.system.menu.model.dto.MenuUpdateDTO;
import com.yisen.module.system.menu.model.po.SysMenu;
import com.yisen.module.system.menu.model.vo.MenuTreeVO;

import java.util.List;

/**
 * 系统菜单服务
 *
 * @author rainluo
 * @date 2025-11-14
 */
public interface SysMenuService extends IService<SysMenu> {

    /**
     * 查询所有菜单（平铺列表，用于管理）
     *
     * @param queryDTO 查询参数
     * @return 菜单列表
     */
    List<MenuTreeVO> getAllMenus(MenuQueryDTO queryDTO);

    /**
     * 获取菜单树形结构（前端渲染侧边栏）
     *
     * @return 菜单树
     */
    List<MenuTreeVO> getMenuTree();

    /**
     * 获取当前用户可用菜单树（带权限过滤）
     *
     * @param userId 用户ID
     * @return 菜单树
     */
    List<MenuTreeVO> getUserMenuTree(String userId);

    /**
     * 新增菜单
     *
     * @param dto 新增菜单DTO
     * @return 菜单ID
     */
    String addMenu(MenuAddDTO dto);

    /**
     * 获取菜单详情
     *
     * @param id 菜单ID
     * @return 菜单详情
     */
    MenuTreeVO getMenuDetail(String id);

    /**
     * 修改菜单
     *
     * @param dto 修改菜单DTO
     */
    void updateMenu(MenuUpdateDTO dto);

    /**
     * 删除菜单
     *
     * @param id 菜单ID
     */
    void deleteMenu(String id);

}

