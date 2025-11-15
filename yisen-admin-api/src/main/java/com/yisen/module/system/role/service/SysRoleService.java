package com.yisen.module.system.role.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yisen.common.model.PageRequest;
import com.yisen.common.model.PageResult;
import com.yisen.module.system.role.model.dto.RoleAddDTO;
import com.yisen.module.system.role.model.dto.RoleMenuAssignDTO;
import com.yisen.module.system.role.model.dto.RoleQueryDTO;
import com.yisen.module.system.role.model.dto.RoleUpdateDTO;
import com.yisen.module.system.role.model.po.SysRole;
import com.yisen.module.system.role.model.vo.RoleDetailVO;

/**
 * 系统角色服务
 *
 * @author rainluo
 * @date 2025-11-14
 */
public interface SysRoleService extends IService<SysRole> {

    /**
     * 分页查询角色列表
     *
     * @param pageRequest 分页请求参数
     * @return 分页结果
     */
    PageResult<RoleDetailVO> pageRoles(PageRequest<RoleQueryDTO> pageRequest);

    /**
     * 新增角色
     *
     * @param dto 新增角色DTO
     * @return 角色ID
     */
    String addRole(RoleAddDTO dto);

    /**
     * 获取角色详情（含关联菜单/权限）
     *
     * @param id 角色ID
     * @return 角色详情
     */
    RoleDetailVO getRoleDetail(String id);

    /**
     * 修改角色基本信息
     *
     * @param dto 修改角色DTO
     */
    void updateRole(RoleUpdateDTO dto);

    /**
     * 删除角色
     *
     * @param id 角色ID
     */
    void deleteRole(String id);

    /**
     * 分配角色菜单权限
     *
     * @param dto 分配权限DTO
     */
    void assignMenus(RoleMenuAssignDTO dto);

}

