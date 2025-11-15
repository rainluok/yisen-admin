package com.yisen.module.system.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yisen.module.system.user.model.dto.UserQueryDTO;
import com.yisen.module.system.user.model.po.SysUser;
import com.yisen.module.system.user.model.vo.UserDetailVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 系统用户 Mapper
 *
 * @author rainluo
 * @date 2025-11-13
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 分页查询用户列表（包含角色信息）
     *
     * @param page     分页对象
     * @param queryDTO 查询参数
     * @return 用户详情列表
     */
    IPage<UserDetailVO> selectUserPageWithRoles(@Param("page") Page<UserDetailVO> page,
                                                @Param("query") UserQueryDTO queryDTO);

    /**
     * 根据ID查询用户详情（包含角色信息）
     *
     * @param id 用户ID
     * @return 用户详情
     */
    UserDetailVO selectUserDetailById(@Param("id") String id);

    /**
     * 批量查询用户列表（包含角色信息）
     *
     * @param userIds 用户ID列表
     * @return 用户详情列表
     */
    List<UserDetailVO> selectUserListWithRoles(@Param("userIds") List<String> userIds);

    /**
     * 查询用户的角色编码集合
     *
     * @param userId 用户ID
     * @return 角色编码集合
     */
    Set<String> selectRoleCodesByUserId(@Param("userId") String userId);

    /**
     * 查询用户的权限标识集合
     *
     * @param userId 用户ID
     * @return 权限标识集合
     */
    Set<String> selectPermissionsByUserId(@Param("userId") String userId);

}





