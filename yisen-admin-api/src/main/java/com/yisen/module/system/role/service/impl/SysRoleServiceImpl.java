package com.yisen.module.system.role.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yisen.common.enums.ResponseCodeEnum;
import com.yisen.common.exception.BusinessException;
import com.yisen.common.model.PageRequest;
import com.yisen.common.model.PageResult;
import com.yisen.module.system.menu.mapper.SysMenuMapper;
import com.yisen.module.system.role.mapper.SysRoleMapper;
import com.yisen.module.system.role.mapper.SysRoleMenuMapper;
import com.yisen.module.system.role.model.dto.RoleAddDTO;
import com.yisen.module.system.role.model.dto.RoleMenuAssignDTO;
import com.yisen.module.system.role.model.dto.RoleQueryDTO;
import com.yisen.module.system.role.model.dto.RoleUpdateDTO;
import com.yisen.module.system.role.model.po.SysRole;
import com.yisen.module.system.role.model.po.SysRoleMenu;
import com.yisen.module.system.role.model.vo.RoleDetailVO;
import com.yisen.module.system.role.service.SysRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 系统角色服务实现
 *
 * @author rainluo
 * @date 2025-11-14
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole>
        implements SysRoleService {

    private final SysRoleMenuMapper sysRoleMenuMapper;
    private final SysMenuMapper sysMenuMapper;

    @Override
    public PageResult<RoleDetailVO> pageRoles(PageRequest<RoleQueryDTO> pageRequest) {
        // 构建分页对象
        Page<RoleDetailVO> page = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());

        // 使用 XML 方式查询（一条 SQL 完成多表关联）
        RoleQueryDTO queryDTO = pageRequest.getParams();
        IPage<RoleDetailVO> pageResult = baseMapper.selectRolePageWithMenus(page, queryDTO);

        // 返回分页结果
        return PageResult.of(pageResult);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addRole(RoleAddDTO dto) {
        // 检查角色编码是否已存在
        Long count = this.lambdaQuery()
                .eq(SysRole::getRoleCode, dto.getRoleCode())
                .eq(SysRole::getIsDeleted, 0)
                .count();
        if (count > 0) {
            throw new BusinessException(ResponseCodeEnum.ROLE_ALREADY_EXISTS);
        }

        // 创建角色
        SysRole role = new SysRole();
        role.setRoleName(dto.getRoleName());
        role.setRoleCode(dto.getRoleCode());
        role.setDescription(dto.getDescription());
        role.setStatus(dto.getStatus());
        role.setIsDeleted(0);
        role.setCreateTime(new Date());
        role.setUpdateTime(new Date());

        this.save(role);

        // 保存角色菜单关系
        if (dto.getMenuIds() != null && !dto.getMenuIds().isEmpty()) {
            saveRoleMenus(role.getId(), dto.getMenuIds());
        }

        log.info("新增角色成功，角色名：{}，ID：{}", dto.getRoleName(), role.getId());
        return role.getId();
    }

    @Override
    public RoleDetailVO getRoleDetail(String id) {
        if (StringUtils.isEmpty(id)) {
            throw new BusinessException(ResponseCodeEnum.BAD_REQUEST);
        }

        // 使用 XML 方式查询（一条 SQL 完成多表关联）
        RoleDetailVO vo = baseMapper.selectRoleDetailById(id);
        if (vo == null) {
            throw new BusinessException(ResponseCodeEnum.ROLE_NOT_FOUND);
        }

        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(RoleUpdateDTO dto) {
        if (StringUtils.isEmpty(dto.getId())) {
            throw new BusinessException(ResponseCodeEnum.BAD_REQUEST);
        }

        SysRole role = this.getById(dto.getId());
        if (role == null) {
            throw new BusinessException(ResponseCodeEnum.ROLE_NOT_FOUND);
        }
        if (role.getIsDeleted() == 1) {
            throw new BusinessException(ResponseCodeEnum.ROLE_DELETED);
        }

        // 更新角色信息
        if (StringUtils.isNotBlank(dto.getRoleName())) {
            role.setRoleName(dto.getRoleName());
        }
        if (StringUtils.isNotBlank(dto.getDescription())) {
            role.setDescription(dto.getDescription());
        }
        if (dto.getStatus() != null) {
            role.setStatus(dto.getStatus());
        }
        role.setUpdateTime(new Date());

        this.updateById(role);

        log.info("修改角色成功，角色ID：{}", dto.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(String id) {
        if (StringUtils.isEmpty(id)) {
            throw new BusinessException(ResponseCodeEnum.BAD_REQUEST);
        }

        SysRole role = this.getById(id);
        if (role == null) {
            throw new BusinessException(ResponseCodeEnum.ROLE_NOT_FOUND);
        }
        if (role.getIsDeleted() == 1) {
            throw new BusinessException(ResponseCodeEnum.ROLE_DELETED);
        }

        // 逻辑删除
        role.setIsDeleted(1);
        role.setUpdateTime(new Date());
        this.updateById(role);

        // 删除角色菜单关系
        sysRoleMenuMapper.delete(
                new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, id)
        );

        log.info("删除角色成功，角色ID：{}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignMenus(RoleMenuAssignDTO dto) {
        if (StringUtils.isEmpty(dto.getRoleId())) {
            throw new BusinessException(ResponseCodeEnum.BAD_REQUEST);
        }
        if (dto.getMenuIds() == null || dto.getMenuIds().isEmpty()) {
            throw new BusinessException(ResponseCodeEnum.MENU_IDS_EMPTY);
        }

        // 检查角色是否存在
        SysRole role = this.getById(dto.getRoleId());
        if (role == null) {
            throw new BusinessException(ResponseCodeEnum.ROLE_NOT_FOUND);
        }
        if (role.getIsDeleted() == 1) {
            throw new BusinessException(ResponseCodeEnum.ROLE_DELETED);
        }

        // 删除旧的角色菜单关系
        sysRoleMenuMapper.delete(
                new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, dto.getRoleId())
        );

        // 保存新的角色菜单关系
        saveRoleMenus(dto.getRoleId(), dto.getMenuIds());

        log.info("分配角色菜单权限成功，角色ID：{}，菜单数量：{}", dto.getRoleId(), dto.getMenuIds().size());
    }

    /**
     * 保存角色菜单关系
     */
    private void saveRoleMenus(String roleId, List<String> menuIds) {
        if (menuIds == null || menuIds.isEmpty()) {
            return;
        }

        List<SysRoleMenu> roleMenus = new ArrayList<>();
        for (String menuId : menuIds) {
            SysRoleMenu roleMenu = new SysRoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            roleMenus.add(roleMenu);
        }

        roleMenus.forEach(sysRoleMenuMapper::insert);
    }

    /**
     * 转换为详情 VO
     */
    private RoleDetailVO convertToDetailVO(SysRole role) {
        RoleDetailVO vo = new RoleDetailVO();
        BeanUtils.copyProperties(role, vo);
        return vo;
    }

}
