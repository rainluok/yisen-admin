package com.yisen.common.annotation;

import java.lang.annotation.*;

/**
 * 接口限流注解
 *
 * @author rainluo
 * @date 2025-11-14
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {

    /**
     * 限流类型，默认基于IP
     */
    LimitType limitType() default LimitType.IP;

    /**
     * 限流次数（每秒）
     */
    int limit() default 10;

    /**
     * 限流类型
     */
    enum LimitType {
        /**
         * 基于 IP 限流
         */
        IP,
        /**
         * 基于用户限流
         */
        USER
    }
}

