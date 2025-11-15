package com.yisen.module.system.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yisen.common.model.PageRequest;
import com.yisen.common.model.PageResult;
import com.yisen.module.system.user.model.dto.*;
import com.yisen.module.system.user.model.po.SysUser;
import com.yisen.module.system.user.model.vo.LoginInfoVO;
import com.yisen.module.system.user.model.vo.UserDetailVO;

/**
 * 系统用户服务
 *
 * @author rainluo
 * @date 2025-11-13
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 登录
     *
     * @param dto 登录DTO
     * @return 登录信息
     */
    LoginInfoVO login(LoginDTO dto);

    /**
     * 分页查询用户列表
     *
     * @param pageRequest 分页请求参数
     * @return 分页结果
     */
    PageResult<UserDetailVO> pageUsers(PageRequest<UserQueryDTO> pageRequest);

    /**
     * 新增用户
     *
     * @param dto 新增用户DTO
     * @return 用户ID
     */
    String addUser(UserAddDTO dto);

    /**
     * 获取用户详情
     *
     * @param id 用户ID
     * @return 用户详情
     */
    UserDetailVO getUserDetail(String id);

    /**
     * 修改用户信息
     *
     * @param dto 修改用户DTO
     */
    void updateUser(UserUpdateDTO dto);

    /**
     * 删除用户（逻辑删除）
     *
     * @param id 用户ID
     */
    void deleteUser(String id);

    /**
     * 启用/禁用用户
     *
     * @param id     用户ID
     * @param status 状态 0-禁用 1-启用
     */
    void updateUserStatus(String id, Integer status);

    /**
     * 重置用户密码
     *
     * @param dto 重置密码DTO
     */
    void resetPassword(UserResetPasswordDTO dto);

}
