package com.yisen.common.aspect;

import com.yisen.common.annotation.RequirePermission;
import com.yisen.common.annotation.RequireRole;
import com.yisen.common.enums.ResponseCodeEnum;
import com.yisen.common.exception.BusinessException;
import com.yisen.core.context.LoginUserContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * 权限校验切面（角色权限 + 功能权限）
 * 使用 AOP 实现权限校验，替代 PermissionInterceptor
 * 支持 @RequireRole 和 @RequirePermission 注解
 *
 * @author rainluo
 * @date 2025-11-14
 */
@Slf4j
@Aspect
@Component
@Order(30) // 确保在认证拦截器之后执行
public class PermissionAspect {

    /**
     * 前置通知：检查角色权限
     */
    @Before("@annotation(requireRole)")
    public void checkRole(JoinPoint joinPoint, RequireRole requireRole) {
        String[] requiredRoles = requireRole.value();
        boolean requireAll = requireRole.requireAll();

        Set<String> userRoles = LoginUserContext.getUser().getRoles();

        if (requireAll) {
            // 需要拥有所有角色
            for (String role : requiredRoles) {
                if (!userRoles.contains(role)) {
                    log.warn("用户缺少必需角色: {}, 当前角色: {}", role, userRoles);
                    throw new BusinessException(ResponseCodeEnum.FORBIDDEN);
                }
            }
        } else {
            // 需要拥有任一角色
            boolean hasAnyRole = false;
            for (String role : requiredRoles) {
                if (userRoles.contains(role)) {
                    hasAnyRole = true;
                    break;
                }
            }
            if (!hasAnyRole) {
                log.warn("用户不具备任何所需角色, 需要: {}, 当前: {}", requiredRoles, userRoles);
                throw new BusinessException(ResponseCodeEnum.FORBIDDEN);
            }
        }

        log.debug("角色权限校验通过: {}", (Object) requiredRoles);
    }

    /**
     * 前置通知：检查功能权限
     */
    @Before("@annotation(requirePermission)")
    public void checkPermission(JoinPoint joinPoint, RequirePermission requirePermission) {
        String[] requiredPermissions = requirePermission.value();
        boolean requireAll = requirePermission.requireAll();

        Set<String> userPermissions = LoginUserContext.getUser().getPermissions();

        if (requireAll) {
            // 需要拥有所有权限
            for (String permission : requiredPermissions) {
                if (!userPermissions.contains(permission)) {
                    log.warn("用户缺少必需权限: {}, 当前权限: {}", permission, userPermissions);
                    throw new BusinessException(ResponseCodeEnum.FORBIDDEN);
                }
            }
        } else {
            // 需要拥有任一权限
            boolean hasAnyPermission = false;
            for (String permission : requiredPermissions) {
                if (userPermissions.contains(permission)) {
                    hasAnyPermission = true;
                    break;
                }
            }
            if (!hasAnyPermission) {
                log.warn("用户不具备任何所需权限, 需要: {}, 当前: {}", requiredPermissions, userPermissions);
                throw new BusinessException(ResponseCodeEnum.FORBIDDEN);
            }
        }

        log.debug("功能权限校验通过: {}", (Object) requiredPermissions);
    }

    /**
     * 类级别的角色权限检查
     * 处理类上的 @RequireRole 注解
     */
    @Before("@within(requireRole) && execution(* com.yisen..*.*(..))")
    public void checkRoleOnClass(JoinPoint joinPoint, RequireRole requireRole) {
        // 先检查方法上是否有注解，如果有则跳过（方法级别优先）
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        if (method.isAnnotationPresent(RequireRole.class)) {
            return;
        }

        // 执行类级别的角色检查
        checkRole(joinPoint, requireRole);
    }

    /**
     * 类级别的功能权限检查
     * 处理类上的 @RequirePermission 注解
     */
    @Before("@within(requirePermission) && execution(* com.yisen..*.*(..))")
    public void checkPermissionOnClass(JoinPoint joinPoint, RequirePermission requirePermission) {
        // 先检查方法上是否有注解，如果有则跳过（方法级别优先）
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        if (method.isAnnotationPresent(RequirePermission.class)) {
            return;
        }

        // 执行类级别的权限检查
        checkPermission(joinPoint, requirePermission);
    }
}

