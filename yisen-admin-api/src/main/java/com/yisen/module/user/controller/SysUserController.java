package com.yisen.module.user.controller;

import com.yisen.common.enums.ResponseCodeEnum;
import com.yisen.common.exception.BusinessException;
import com.yisen.common.result.Result;
import com.yisen.common.util.Assert;
import com.yisen.module.user.model.dto.LoginDTO;
import com.yisen.module.user.service.SysUserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户相关接口
 * 
 * @author yisen
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 用户登录
     *
     * @param loginDTO 登录参数
     * @return 登录结果
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Valid @RequestBody LoginDTO loginDTO) {
        // 使用断言校验参数
        Assert.notBlank(loginDTO.getUsername(), "用户名不能为空");
        Assert.notBlank(loginDTO.getPassword(), "密码不能为空");
        
        // TODO: 实现真实的登录逻辑
        // 示例：模拟登录
        Map<String, Object> result = new HashMap<>();
        result.put("token", "mock_token_" + System.currentTimeMillis());
        result.put("username", loginDTO.getUsername());
        
        return Result.success("登录成功", result);
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("/info")
    public Result<Map<String, Object>> getUserInfo() {
        // TODO: 从token中获取用户ID，查询用户信息
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", 1);
        userInfo.put("username", "admin");
        userInfo.put("name", "管理员");
        userInfo.put("avatar", "/static/default.png");
        userInfo.put("email", "admin@yisen.com");
        userInfo.put("roles", List.of("admin"));
        userInfo.put("permissions", List.of("user:view", "user:create", "user:update", "user:delete"));
        
        return Result.success(userInfo);
    }

    /**
     * 用户登出
     *
     * @return 登出结果
     */
    @PostMapping("/logout")
    public Result<Void> logout() {
        // TODO: 清除token，记录登出日志
        log.info("用户登出");
        return Result.success("登出成功");
    }

    /**
     * 修改用户信息
     *
     * @param userInfo 用户信息
     * @return 修改结果
     */
    @PutMapping("/update")
    public Result<Void> updateUserInfo(@RequestBody Map<String, Object> userInfo) {
        Assert.notNull(userInfo, "用户信息不能为空");
        
        // TODO: 实现真实的更新逻辑
        log.info("更新用户信息: {}", userInfo);
        
        return Result.success("用户信息更新成功");
    }

    /**
     * 修改密码
     *
     * @param passwordData 密码数据 { "oldPassword": "...", "newPassword": "..." }
     * @return 修改结果
     */
    @PutMapping("/password")
    public Result<Void> updatePassword(@RequestBody Map<String, String> passwordData) {
        String oldPassword = passwordData.get("oldPassword");
        String newPassword = passwordData.get("newPassword");
        
        Assert.notBlank(oldPassword, "原密码不能为空");
        Assert.notBlank(newPassword, "新密码不能为空");
        Assert.isTrue(newPassword.length() >= 6, "新密码长度不能少于6位");
        
        // TODO: 实现真实的密码修改逻辑
        // 1. 验证原密码
        // 2. 加密新密码
        // 3. 更新数据库
        
        log.info("修改密码成功");
        return Result.success("密码修改成功");
    }

    /**
     * 获取用户列表
     *
     * @param page     页码
     * @param pageSize 每页数量
     * @param username 用户名（可选）
     * @param status   状态（可选）
     * @return 用户列表
     */
    @GetMapping("/list")
    public Result<Map<String, Object>> getUserList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Integer status) {
        
        Assert.isTrue(page > 0, "页码必须大于0");
        Assert.isTrue(pageSize > 0 && pageSize <= 100, "每页数量必须在1-100之间");
        
        // TODO: 实现真实的分页查询逻辑
        List<Map<String, Object>> userList = List.of(
            createUserMap(1, "admin", "管理员", "admin@yisen.com", 1),
            createUserMap(2, "test", "测试用户", "test@yisen.com", 1)
        );
        
        Map<String, Object> result = new HashMap<>();
        result.put("list", userList);
        result.put("total", 2);
        result.put("page", page);
        result.put("pageSize", pageSize);
        
        return Result.success(result);
    }
    
    /**
     * 创建用户信息Map
     */
    private Map<String, Object> createUserMap(Integer id, String username, String name, 
                                               String email, Integer status) {
        Map<String, Object> user = new HashMap<>();
        user.put("id", id);
        user.put("username", username);
        user.put("name", name);
        user.put("email", email);
        user.put("status", status);
        user.put("createTime", "2024-01-01 10:00:00");
        return user;
    }
}
