package com.yisen.common.annotation;

import java.lang.annotation.*;

/**
 * 防重复提交注解
 *
 * @author rainluo
 * @date 2025-11-14
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RepeatSubmit {

    /**
     * 防重复提交时间间隔（秒）
     */
    long interval() default 5;
}

