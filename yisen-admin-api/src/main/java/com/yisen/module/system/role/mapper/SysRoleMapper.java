package com.yisen.module.system.role.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yisen.module.system.role.model.dto.RoleQueryDTO;
import com.yisen.module.system.role.model.po.SysRole;
import com.yisen.module.system.role.model.vo.RoleDetailVO;
import org.apache.ibatis.annotations.Param;

/**
 * 系统角色 Mapper
 *
 * @author rainluo
 * @date 2025-11-14
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 分页查询角色列表（包含菜单信息）
     *
     * @param page     分页对象
     * @param queryDTO 查询参数
     * @return 角色详情列表
     */
    IPage<RoleDetailVO> selectRolePageWithMenus(@Param("page") Page<RoleDetailVO> page,
                                                @Param("query") RoleQueryDTO queryDTO);

    /**
     * 根据ID查询角色详情（包含菜单信息）
     *
     * @param id 角色ID
     * @return 角色详情
     */
    RoleDetailVO selectRoleDetailById(@Param("id") String id);

}





