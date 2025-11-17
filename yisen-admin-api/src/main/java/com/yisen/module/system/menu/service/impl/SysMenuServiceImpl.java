package com.yisen.module.system.menu.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yisen.common.enums.ResponseCodeEnum;
import com.yisen.common.exception.BusinessException;
import com.yisen.module.system.menu.mapper.SysMenuMapper;
import com.yisen.module.system.menu.model.dto.MenuAddDTO;
import com.yisen.module.system.menu.model.dto.MenuQueryDTO;
import com.yisen.module.system.menu.model.dto.MenuUpdateDTO;
import com.yisen.module.system.menu.model.po.SysMenu;
import com.yisen.module.system.menu.model.vo.MenuTreeVO;
import com.yisen.module.system.menu.service.SysMenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 系统菜单服务实现
 *
 * @author rainluo
 * @date 2025-11-14
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu>
        implements SysMenuService {

    @Override
    public List<MenuTreeVO> getAllMenus(MenuQueryDTO queryDTO) {
        // 构建查询条件
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMenu::getIsDeleted, 0);

        if (queryDTO != null) {
            wrapper.like(StringUtils.isNotBlank(queryDTO.getMenuName()), SysMenu::getMenuName, queryDTO.getMenuName())
                    .like(StringUtils.isNotBlank(queryDTO.getMenuCode()), SysMenu::getMenuCode, queryDTO.getMenuCode())
                    .eq(queryDTO.getType() != null, SysMenu::getType, queryDTO.getType())
                    .eq(queryDTO.getStatus() != null, SysMenu::getStatus, queryDTO.getStatus());
        }

        wrapper.orderByAsc(SysMenu::getSort, SysMenu::getCreateTime);

        // 查询所有菜单
        List<SysMenu> menuList = this.list(wrapper);

        // 转换为 VO
        return menuList.stream()
                .map(this::convertToTreeVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MenuTreeVO> getMenuTree() {
        // 使用 XML 查询所有菜单
        List<MenuTreeVO> allMenus = baseMapper.selectAllMenuList();

        // 构建树形结构
        return buildMenuTree(allMenus, "0");
    }

    @Override
    public List<MenuTreeVO> getUserMenuTree(String userId) {
        if (StringUtils.isEmpty(userId)) {
            throw new BusinessException(ResponseCodeEnum.BAD_REQUEST);
        }

        // 使用 XML 查询用户可用菜单
        List<MenuTreeVO> userMenus = baseMapper.selectMenuListByUserId(userId);

        // 构建树形结构
        List<MenuTreeVO> menuTreeVOS = buildMenuTree(userMenus, "0");
        return menuTreeVOS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addMenu(MenuAddDTO dto) {
        // 检查菜单编码是否已存在
        Long count = this.lambdaQuery()
                .eq(SysMenu::getMenuCode, dto.getMenuCode())
                .eq(SysMenu::getIsDeleted, 0)
                .count();
        if (count > 0) {
            throw new BusinessException(ResponseCodeEnum.MENU_CODE_EXISTS);
        }

        // 创建菜单
        SysMenu menu = new SysMenu();
        BeanUtils.copyProperties(dto, menu);
        menu.setIsDeleted(0);

        this.save(menu);

        log.info("新增菜单成功，菜单名：{}，ID：{}", dto.getMenuName(), menu.getId());
        return menu.getId();
    }

    @Override
    public MenuTreeVO getMenuDetail(String id) {
        if (StringUtils.isEmpty(id)) {
            throw new BusinessException(ResponseCodeEnum.BAD_REQUEST);
        }

        SysMenu menu = this.getById(id);
        if (menu == null) {
            throw new BusinessException(ResponseCodeEnum.MENU_NOT_FOUND);
        }
        if (menu.getIsDeleted() == 1) {
            throw new BusinessException(ResponseCodeEnum.MENU_DELETED);
        }

        return convertToTreeVO(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMenu(MenuUpdateDTO dto) {
        if (StringUtils.isEmpty(dto.getId())) {
            throw new BusinessException(ResponseCodeEnum.BAD_REQUEST);
        }

        SysMenu menu = this.getById(dto.getId());
        if (menu == null) {
            throw new BusinessException(ResponseCodeEnum.MENU_NOT_FOUND);
        }
        if (menu.getIsDeleted() == 1) {
            throw new BusinessException(ResponseCodeEnum.MENU_DELETED);
        }

        // 更新菜单信息
        if (StringUtils.isNotBlank(dto.getMenuName())) {
            menu.setMenuName(dto.getMenuName());
        }
        if (StringUtils.isNotBlank(dto.getPath())) {
            menu.setPath(dto.getPath());
        }
        if (StringUtils.isNotBlank(dto.getComponent())) {
            menu.setComponent(dto.getComponent());
        }
        if (StringUtils.isNotBlank(dto.getPermission())) {
            menu.setPermission(dto.getPermission());
        }
        if (StringUtils.isNotBlank(dto.getIcon())) {
            menu.setIcon(dto.getIcon());
        }
        if (dto.getHidden() != null) {
            menu.setHidden(dto.getHidden());
        }
        if (StringUtils.isNotBlank(dto.getLayout())) {
            menu.setLayout(dto.getLayout());
        }
        if (dto.getSort() != null) {
            menu.setSort(dto.getSort());
        }
        if (dto.getStatus() != null) {
            menu.setStatus(dto.getStatus());
        }

        this.updateById(menu);

        log.info("修改菜单成功，菜单ID：{}", dto.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMenu(String id) {
        if (StringUtils.isEmpty(id)) {
            throw new BusinessException(ResponseCodeEnum.BAD_REQUEST);
        }

        SysMenu menu = this.getById(id);
        if (menu == null) {
            throw new BusinessException(ResponseCodeEnum.MENU_NOT_FOUND);
        }
        if (menu.getIsDeleted() == 1) {
            throw new BusinessException(ResponseCodeEnum.MENU_DELETED);
        }

        // 检查是否有子菜单
        Long childCount = this.lambdaQuery()
                .eq(SysMenu::getParentId, id)
                .eq(SysMenu::getIsDeleted, 0)
                .count();
        if (childCount > 0) {
            throw new BusinessException(ResponseCodeEnum.MENU_HAS_CHILDREN);
        }

        // 逻辑删除
        menu.setIsDeleted(1);
        this.updateById(menu);

        log.info("删除菜单成功，菜单ID：{}", id);
    }

    /**
     * 构建菜单树（非递归，O(n) 时间复杂度）
     */
    public List<MenuTreeVO> buildMenuTree(List<MenuTreeVO> allMenus, String rootParentId) {
        if (allMenus == null || allMenus.isEmpty()) {
            return Collections.emptyList();
        }

        // 1. 创建 id -> menu 的映射
        Map<String, MenuTreeVO> menuMap = new HashMap<>();
        for (MenuTreeVO menu : allMenus) {
            // 初始化 children 避免 null
            menu.setChildren(new ArrayList<>());
            menuMap.put(menu.getId(), menu);
        }

        // 2. 构建树结构
        List<MenuTreeVO> roots = new ArrayList<>();
        for (MenuTreeVO menu : allMenus) {
            String parentId = menu.getParentId();

            // 处理根节点：parentId 为 null、空、或等于 rootParentId
            if (parentId == null || parentId.isEmpty() || rootParentId.equals(parentId)) {
                roots.add(menu);
            } else {
                // 尝试挂到父节点下
                MenuTreeVO parent = menuMap.get(parentId);
                if (parent != null) {
                    parent.getChildren().add(menu);
                } else {
                    // 孤儿节点（父节点不存在），可选择忽略或加入根节点
                    // 这里建议记录日志，便于排查数据问题
                    log.warn("Menu[id={}] has invalid parentId: {}", menu.getId(), parentId);
                    roots.add(menu); // 可选：作为根节点展示
                }
            }
        }

        return roots;
    }

    /**
     * 转换为树形 VO
     */
    private MenuTreeVO convertToTreeVO(SysMenu menu) {
        MenuTreeVO vo = new MenuTreeVO();
        BeanUtils.copyProperties(menu, vo);
        return vo;
    }

}
