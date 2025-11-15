package com.yisen.common.util;

import com.yisen.common.constant.CacheKey;
import com.yisen.common.service.RedisService;
import com.yisen.module.system.user.model.vo.UserInfoVO;
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
    private static final ThreadLocal<UserInfoVO> USER_CONTEXT = new InheritableThreadLocal<>();


    @Resource
    private RedisService redisService;

    /**
     * 获取用户信息（优先线程上下文，其次Redis），支持多端/分布式应用
     *
     * @return 当前用户信息，若无则为null
     */
    public UserInfoVO getCurrentUser() {
        // 优先从ThreadLocal获取，提高性能
        UserInfoVO userInfoVO = USER_CONTEXT.get();
        if (userInfoVO != null) {
            return userInfoVO;
        }
        HttpServletRequest request = SpringContextUtils.getCurrentHttpRequest();
        if (request == null) {
            return null;
        }
        String token = request.getHeader("Authorization");
        if (token != null && !token.isEmpty()) {
            // Redis中查找
            userInfoVO = (UserInfoVO) redisService.get(CacheKey.AUTH_TOKEN + token);
            return userInfoVO;
        }
        return null;
    }

    /**
     * 设置当前线程用户信息（请求入口控制器层调用）
     *
     * @param userInfoVO 用户信息
     */
    public void setUserInfo(UserInfoVO userInfoVO) {
        USER_CONTEXT.set(userInfoVO);
    }

    /**
     * 清理当前线程用户信息，防止内存泄漏（在请求结束时调用，建议结合拦截器）
     */
    public void clearUserInfo() {
        USER_CONTEXT.remove();
    }

    /**
     * 获取当前线程内用户ID（工具方法）
     *
     * @return 用户ID或null
     */
    public String getCurrentUserId() {
        UserInfoVO userInfoVO = USER_CONTEXT.get();
        return userInfoVO != null ? userInfoVO.getId() : null;
    }

    public String getCurrentUsername() {
        UserInfoVO userInfoVO = USER_CONTEXT.get();
        return userInfoVO != null ? userInfoVO.getUsername() : null;
    }

    public Set<String> getCurrentUserRoles() {
        UserInfoVO userInfoVO = USER_CONTEXT.get();
        return userInfoVO != null ? userInfoVO.getRoles() : null;
    }

    public Set<String> getCurrentUserPermissions() {
        UserInfoVO userInfoVO = USER_CONTEXT.get();
        return userInfoVO != null ? userInfoVO.getPermissions() : null;
    }
}
