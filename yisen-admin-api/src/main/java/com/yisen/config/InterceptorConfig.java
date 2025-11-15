package com.yisen.config;

import com.yisen.common.interceptor.AuthenticationInterceptor;
import com.yisen.common.interceptor.OnlineUserInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 配置：配置拦截器执行顺序
 * <p>
 * 说明：
 * - 认证拦截器（AuthenticationInterceptor）：Token 验证，用户上下文注入
 * - 在线用户拦截器（OnlineUserInterceptor）：更新用户在线状态
 * - 其他功能已改为 AOP 切面实现：
 * 1. @RateLimit - RateLimitAspect（限流）
 * 2. @RepeatSubmit - RepeatSubmitAspect（防重复提交）
 * 3. @RequireRole/@RequirePermission - PermissionAspect（权限校验）
 * 4. @OperationLog - OperationLogAspect（操作日志）
 * 5. @Sensitive - SensitiveDataAspect（敏感数据脱敏）
 *
 * @author rainluo
 * @date 2025-11-14
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    /**
     * 静态资源路径
     */
    private static final String[] EXCLUDE_PATHS = {
            "/static/**",
            "/favicon.ico",
            "/error",
            "/doc.html",
            "/webjars/**",
            "/swagger-resources/**",
            "/v3/api-docs/**"
    };
    /**
     * 不需要认证的接口
     */
    private static final String[] PUBLIC_PATHS = {
            "/login",
            "/captcha"
    };
    @Resource
    private AuthenticationInterceptor authenticationInterceptor;
    @Resource
    private OnlineUserInterceptor onlineUserInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 1. 认证拦截器（验证Token，注入用户上下文）
        registry.addInterceptor(authenticationInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(EXCLUDE_PATHS)
                .excludePathPatterns(PUBLIC_PATHS)
                .order(1);

        // 2. 在线用户心跳拦截器（更新在线状态）
        registry.addInterceptor(onlineUserInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(EXCLUDE_PATHS)
                .excludePathPatterns(PUBLIC_PATHS)
                .order(2);

        // 注意：其他功能已改为 AOP 切面实现，无需在此配置
    }
}

