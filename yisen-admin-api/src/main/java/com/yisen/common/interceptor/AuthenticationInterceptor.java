package com.yisen.common.interceptor;

import com.yisen.common.annotation.Anonymous;
import com.yisen.common.enums.ResponseCodeEnum;
import com.yisen.common.exception.BusinessException;
import com.yisen.common.util.JwtUtil;
import com.yisen.common.util.UserUtil;
import com.yisen.module.system.user.model.vo.UserInfoVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;

/**
 * 认证拦截器：验证 Token，注入用户上下文
 * 支持 @Anonymous 注解，标记的方法或类无需认证
 *
 * @author rainluo
 * @date 2025-11-14
 */
@Slf4j
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private UserUtil userUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 如果不是方法处理器，直接放行
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        // 检查方法上是否有 @Anonymous 注解
        if (method.isAnnotationPresent(Anonymous.class)) {
            log.debug("方法标记为 @Anonymous，跳过认证: {}", method.getName());
            return true;
        }

        // 检查类上是否有 @Anonymous 注解
        if (handlerMethod.getBeanType().isAnnotationPresent(Anonymous.class)) {
            log.debug("类标记为 @Anonymous，跳过认证: {}", handlerMethod.getBeanType().getName());
            return true;
        }

        // 获取 Token
        String token = jwtUtil.getToken(request);

        if (StringUtils.isEmpty(token)) {
            log.warn("请求未携带 Token: {}", request.getRequestURI());
            throw new BusinessException(ResponseCodeEnum.UNAUTHORIZED);
        }

        // 验证 Token
        if (!jwtUtil.validateToken(token)) {
            log.warn("Token 验证失败: {}", request.getRequestURI());
            throw new BusinessException(ResponseCodeEnum.TOKEN_INVALID);
        }

        // 解析用户信息并注入上下文
        UserInfoVO userInfo = jwtUtil.getUserInfoFromToken(token);
        if (userInfo == null) {
            log.warn("Token 中无用户信息: {}", request.getRequestURI());
            throw new BusinessException(ResponseCodeEnum.TOKEN_INVALID);
        }

        // 将用户信息注入到 ThreadLocal
        userUtil.setUserInfo(userInfo);

        log.debug("认证成功，用户: {}, URI: {}", userInfo.getUsername(), request.getRequestURI());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        // 清理 ThreadLocal，避免内存泄漏
        userUtil.clearUserInfo();
    }
}

