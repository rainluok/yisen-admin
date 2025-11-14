package com.yisen.module.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yisen.module.user.model.po.SysUser;
import com.yisen.module.user.service.SysUserService;
import com.yisen.module.user.mapper.SysUserMapper;
import org.springframework.stereotype.Service;

/**
* @author rainluo
* @description 针对表【sys_user(系统用户表)】的数据库操作Service实现
* @createDate 2025-11-13 20:33:52
*/
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
    implements SysUserService{

}




