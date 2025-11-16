package com.yisen.common.util;

import com.yisen.common.constant.CacheKey;
import com.yisen.common.service.RedisService;
import com.yisen.module.system.user.model.vo.LoginUserVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author rainluo
 * @version 1.1
 * @description 用户工具类：管理Token生成、用户上下文、用户信息获取
 * @date 2025/11/14 10:18
 */
@Component
public class UserUtil {

    /**
     * 使用 InheritableThreadLocal 便于异步/子线程传递用户信息
     */
    private static final ThreadLocal<LoginUserVO> USER_CONTEXT = new InheritableThreadLocal<>();


    @Resource
    private RedisService redisService;

    /**
     * 获取用户信息
     *
     * @return 当前用户信息，若无则为null
     */
    public LoginUserVO getUser() {
        return USER_CONTEXT.get();
    }

    /**
     * 设置当前线程用户信息（请求入口控制器层调用）
     *
     * @param loginUser 用户信息
     */
    public void setUser(LoginUserVO loginUser) {
        USER_CONTEXT.set(loginUser);
    }

    /**
     * 清理当前线程用户信息，防止内存泄漏（在请求结束时调用，建议结合拦截器）
     */
    public void clear() {
        USER_CONTEXT.remove();
    }

    /**
     * 获取当前线程内用户ID（工具方法）
     *
     * @return 用户ID或null
     */
    public String getUserId() {
        LoginUserVO loginUser = USER_CONTEXT.get();
        return loginUser != null ? loginUser.getId() : null;
    }

    public String getUsername() {
        LoginUserVO loginUser = USER_CONTEXT.get();
        return loginUser != null ? loginUser.getUsername() : null;
    }

    public Set<String> getUserRoles() {
        LoginUserVO loginUser = USER_CONTEXT.get();
        return loginUser != null ? loginUser.getRoles() : null;
    }

    public Set<String> getCurrentUserPermissions() {
        LoginUserVO loginUser = USER_CONTEXT.get();
        return loginUser != null ? loginUser.getPermissions() : null;
    }
}
