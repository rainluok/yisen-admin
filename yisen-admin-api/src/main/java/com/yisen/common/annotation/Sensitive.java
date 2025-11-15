package com.yisen.common.annotation;

import com.yisen.common.enums.SensitiveType;

import java.lang.annotation.*;

/**
 * 敏感数据注解
 *
 * @author rainluo
 * @date 2025-11-14
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Sensitive {

    /**
     * 脱敏类型
     */
    SensitiveType type();
}

