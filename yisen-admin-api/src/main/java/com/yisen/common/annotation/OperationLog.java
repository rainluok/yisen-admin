package com.yisen.common.annotation;

import com.yisen.common.enums.BusinessType;
import com.yisen.common.enums.OperationType;

import java.lang.annotation.*;

/**
 * 操作日志注解
 *
 * @author rainluo
 * @date 2025-11-14
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLog {

    /**
     * 操作描述
     */
    String value();

    /**
     * 操作类型（新增、修改、删除、查询等）
     */
    OperationType type() default OperationType.SELECT;

    /**
     * 业务类型（用户、角色、菜单等）
     */
    BusinessType bizType() default BusinessType.OTHER;

    /**
     * 业务ID（可选，用于关联具体的业务数据）
     */
    String bizId() default "";
}

