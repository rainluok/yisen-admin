package com.yisen.common.interceptor;

import com.yisen.common.util.UserUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.TimeUnit;

/**
 * 在线用户心跳拦截器
 *
 * @author rainluo
 * @date 2025-11-14
 */
@Slf4j
@Component
public class OnlineUserInterceptor implements HandlerInterceptor {

    private static final String ONLINE_USER_KEY_PREFIX = "online:user:";
    private static final long ONLINE_USER_EXPIRE_MINUTES = 30;
    @Resource
    private UserUtil userUtil;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            String userId = userUtil.getCurrentUserId();
            if (userId != null) {
                // 更新在线状态
                String onlineKey = ONLINE_USER_KEY_PREFIX + userId;
                redisTemplate.opsForValue().set(onlineKey, System.currentTimeMillis(),
                        ONLINE_USER_EXPIRE_MINUTES, TimeUnit.MINUTES);

                log.debug("用户 {} 在线状态已更新", userId);
            }
        } catch (Exception e) {
            // 心跳失败不影响业务
            log.error("更新在线用户状态失败", e);
        }
        return true;
    }
}

