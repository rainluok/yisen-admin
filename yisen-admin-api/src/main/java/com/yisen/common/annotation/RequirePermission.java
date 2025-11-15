package com.yisen.common.annotation;

import java.lang.annotation.*;

/**
 * 功能权限注解
 *
 * @author rainluo
 * @date 2025-11-14
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequirePermission {

    /**
     * 所需权限标识列表
     */
    String[] value();

    /**
     * 是否需要拥有所有权限（true：需要全部，false：需要任一）
     */
    boolean requireAll() default false;
}

