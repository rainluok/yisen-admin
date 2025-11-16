package com.yisen.module.system.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yisen.common.constant.CacheKey;
import com.yisen.common.enums.ResponseCodeEnum;
import com.yisen.common.exception.BusinessException;
import com.yisen.common.model.PageRequest;
import com.yisen.common.model.PageResult;
import com.yisen.common.service.RedisService;
import com.yisen.common.util.JwtUtil;
import com.yisen.common.util.PasswordUtil;
import com.yisen.common.util.UserUtil;
import com.yisen.module.system.role.mapper.SysRoleMapper;
import com.yisen.module.system.user.mapper.SysUserMapper;
import com.yisen.module.system.user.mapper.SysUserRoleMapper;
import com.yisen.module.system.user.model.dto.*;
import com.yisen.module.system.user.model.po.SysUser;
import com.yisen.module.system.user.model.po.SysUserRole;
import com.yisen.module.system.user.model.vo.LoginUserVO;
import com.yisen.module.system.user.model.vo.UserDetailVO;
import com.yisen.module.system.user.model.vo.LoginUserVO;
import com.yisen.module.system.user.service.SysUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 系统用户服务实现
 *
 * @author rainluo
 * @date 2025-11-13
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
        implements SysUserService {

    private final SysUserRoleMapper sysUserRoleMapper;
    private final SysRoleMapper sysRoleMapper;
    private final UserUtil userUtil;
    private final JwtUtil jwtUtil;
    private final RedisService redisService;

    @Override
    public LoginUserVO login(LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            throw new BusinessException(ResponseCodeEnum.USERNAME_PASSWORD_ERROR);
        }
        SysUser user = this.lambdaQuery()
                .eq(SysUser::getUsername, username)
                .eq(SysUser::getIsDeleted, 0)
                .one();
        if (user == null) {
            throw new BusinessException(ResponseCodeEnum.USERNAME_PASSWORD_ERROR);
        }

        Integer status = user.getStatus();
        if (status == 0) {
            throw new BusinessException(ResponseCodeEnum.USER_DISABLED);
        }
        if (!PasswordUtil.matches(password, user.getPassword())) {
            throw new BusinessException(ResponseCodeEnum.USERNAME_PASSWORD_ERROR);
        }

        LoginUserVO loginUser = new LoginUserVO();
        BeanUtils.copyProperties(user, loginUser);

        // 查询用户的角色和权限
        Set<String> roles = baseMapper.selectRoleCodesByUserId(user.getId());
        Set<String> permissions = baseMapper.selectPermissionsByUserId(user.getId());

        loginUser.setRoles(roles);
        loginUser.setPermissions(permissions);

        loginUser.setToken(jwtUtil.generateToken(loginUser));
        return loginUser;
    }

    @Override
    public void logout() {
        redisService.delete(CacheKey.AUTH_TOKEN + userUtil.getUserId());
    }

    @Override
    public PageResult<UserDetailVO> pageUsers(PageRequest<UserQueryDTO> pageRequest) {
        // 构建分页对象
        Page<UserDetailVO> page = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());

        UserQueryDTO queryDTO = pageRequest.getParams();
        IPage<UserDetailVO> pageResult = baseMapper.selectUserPageWithRoles(page, queryDTO);

        // 返回分页结果
        return PageResult.of(pageResult);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addUser(UserAddDTO dto) {
        // 检查用户名是否已存在
        Long count = this.lambdaQuery()
                .eq(SysUser::getUsername, dto.getUsername())
                .eq(SysUser::getIsDeleted, 0)
                .count();
        if (count > 0) {
            throw new BusinessException(ResponseCodeEnum.USER_ALREADY_EXISTS);
        }

        // 检查邮箱是否已存在
        count = this.lambdaQuery()
                .eq(SysUser::getEmail, dto.getEmail())
                .eq(SysUser::getIsDeleted, 0)
                .count();
        if (count > 0) {
            throw new BusinessException(ResponseCodeEnum.EMAIL_ALREADY_EXISTS);
        }

        // 创建用户
        SysUser user = new SysUser();
        user.setUsername(dto.getUsername());
        user.setPassword(PasswordUtil.encode(dto.getPassword())); // 加密密码
        user.setRealName(dto.getRealName());
        user.setGender(dto.getGender());
        user.setAvatar(dto.getAvatar());
        user.setEmail(dto.getEmail());
        user.setStatus(dto.getStatus());
        user.setIsDeleted(0);

        this.save(user);

        // 保存用户角色关系
        if (dto.getRoleIds() != null && !dto.getRoleIds().isEmpty()) {
            saveUserRoles(user.getId(), dto.getRoleIds());
        }

        log.info("新增用户成功，用户名：{}，ID：{}", dto.getUsername(), user.getId());
        return user.getId();
    }

    @Override
    public UserDetailVO getUserDetail(String id) {
        if (StringUtils.isEmpty(id)) {
            throw new BusinessException(ResponseCodeEnum.BAD_REQUEST);
        }

        UserDetailVO vo = baseMapper.selectUserDetailById(id);
        if (vo == null) {
            throw new BusinessException(ResponseCodeEnum.USER_NOT_FOUND);
        }
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(UserUpdateDTO dto) {
        if (StringUtils.isEmpty(dto.getId())) {
            throw new BusinessException(ResponseCodeEnum.BAD_REQUEST);
        }

        SysUser user = this.getById(dto.getId());
        if (user == null) {
            throw new BusinessException(ResponseCodeEnum.USER_NOT_FOUND);
        }
        if (user.getIsDeleted() == 1) {
            throw new BusinessException(ResponseCodeEnum.USER_DELETED);
        }

        // 更新用户信息
        if (StringUtils.isNotBlank(dto.getRealName())) {
            user.setRealName(dto.getRealName());
        }
        if (dto.getGender() != null) {
            user.setGender(dto.getGender());
        }
        if (StringUtils.isNotBlank(dto.getAvatar())) {
            user.setAvatar(dto.getAvatar());
        }
        if (StringUtils.isNotBlank(dto.getEmail())) {
            user.setEmail(dto.getEmail());
        }

        this.updateById(user);

        // 更新用户角色关系
        if (dto.getRoleIds() != null) {
            // 删除旧的角色关系
            sysUserRoleMapper.delete(
                    new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, dto.getId())
            );

            // 保存新的角色关系
            if (!dto.getRoleIds().isEmpty()) {
                saveUserRoles(dto.getId(), dto.getRoleIds());
            }
        }

        log.info("修改用户成功，用户ID：{}", dto.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(String id) {
        if (StringUtils.isEmpty(id)) {
            throw new BusinessException(ResponseCodeEnum.BAD_REQUEST);
        }

        SysUser user = this.getById(id);
        if (user == null) {
            throw new BusinessException(ResponseCodeEnum.USER_NOT_FOUND);
        }
        if (user.getIsDeleted() == 1) {
            throw new BusinessException(ResponseCodeEnum.USER_DELETED);
        }

        // 逻辑删除
        user.setIsDeleted(1);
        this.updateById(user);

        // 删除用户角色关系
        sysUserRoleMapper.delete(
                new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, id)
        );

        log.info("删除用户成功，用户ID：{}", id);
    }

    @Override
    public void updateUserStatus(String id, Integer status) {
        if (StringUtils.isEmpty(id)) {
            throw new BusinessException(ResponseCodeEnum.BAD_REQUEST);
        }
        if (status == null) {
            throw new BusinessException(ResponseCodeEnum.BAD_REQUEST);
        }
        if (status != 0 && status != 1) {
            throw new BusinessException(ResponseCodeEnum.INVALID_STATUS);
        }

        SysUser user = this.getById(id);
        if (user == null) {
            throw new BusinessException(ResponseCodeEnum.USER_NOT_FOUND);
        }
        if (user.getIsDeleted() == 1) {
            throw new BusinessException(ResponseCodeEnum.USER_DELETED);
        }

        user.setStatus(status);
        this.updateById(user);

        log.info("修改用户状态成功，用户ID：{}，状态：{}", id, status == 1 ? "启用" : "禁用");
    }

    @Override
    public void resetPassword(UserResetPasswordDTO dto) {
        if (StringUtils.isEmpty(dto.getId())) {
            throw new BusinessException(ResponseCodeEnum.BAD_REQUEST);
        }
        if (StringUtils.isEmpty(dto.getNewPassword())) {
            throw new BusinessException(ResponseCodeEnum.PASSWORD_ERROR);
        }

        SysUser user = this.getById(dto.getId());
        if (user == null) {
            throw new BusinessException(ResponseCodeEnum.USER_NOT_FOUND);
        }
        if (user.getIsDeleted() == 1) {
            throw new BusinessException(ResponseCodeEnum.USER_DELETED);
        }

        // 加密新密码
        String encodedPassword = PasswordUtil.encode(dto.getNewPassword());
        user.setPassword(encodedPassword);
        this.updateById(user);

        log.info("重置用户密码成功，用户ID：{}", dto.getId());
    }

    /**
     * 保存用户角色关系
     */
    private void saveUserRoles(String userId, List<String> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return;
        }

        List<SysUserRole> userRoles = new ArrayList<>();
        for (String roleId : roleIds) {
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            userRoles.add(userRole);
        }

        userRoles.forEach(sysUserRoleMapper::insert);
    }

}




