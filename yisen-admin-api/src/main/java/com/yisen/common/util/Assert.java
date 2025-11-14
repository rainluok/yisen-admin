package com.yisen.common.util;


import org.apache.commons.lang3.StringUtils;

/**
 * 断言工具类
 * 用于业务逻辑中的断言判断，简化异常抛出
 *
 * @author yisen
 */
public class Assert {

    /**
     * 断言表达式为true，否则抛出异常
     *
     * @param expression 表达式
     * @param message    错误消息
     */
    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new com.yisen.common.exception.BusinessException(message);
        }
    }

    /**
     * 断言表达式为false，否则抛出异常
     *
     * @param expression 表达式
     * @param message    错误消息
     */
    public static void isFalse(boolean expression, String message) {
        if (expression) {
            throw new com.yisen.common.exception.BusinessException(message);
        }
    }

    /**
     * 断言对象不为null，否则抛出异常
     *
     * @param object  对象
     * @param message 错误消息
     */
    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new com.yisen.common.exception.BusinessException(message);
        }
    }

    /**
     * 断言对象为null，否则抛出异常
     *
     * @param object  对象
     * @param message 错误消息
     */
    public static void isNull(Object object, String message) {
        if (object != null) {
            throw new com.yisen.common.exception.BusinessException(message);
        }
    }

    /**
     * 断言字符串不为空，否则抛出异常
     *
     * @param text    字符串
     * @param message 错误消息
     */
    public static void notEmpty(String text, String message) {
        if (StringUtils.isEmpty(text)) {
            throw new com.yisen.common.exception.BusinessException(message);
        }
    }

    /**
     * 断言字符串不为空白，否则抛出异常
     *
     * @param text    字符串
     * @param message 错误消息
     */
    public static void notBlank(String text, String message) {
        if (StringUtils.isBlank(text)) {
            throw new com.yisen.common.exception.BusinessException(message);
        }
    }

    /**
     * 断言集合不为空，否则抛出异常
     *
     * @param collection 集合
     * @param message    错误消息
     */
    public static void notEmpty(java.util.Collection<?> collection, String message) {
        if (collection == null || collection.isEmpty()) {
            throw new com.yisen.common.exception.BusinessException(message);
        }
    }

    /**
     * 断言数组不为空，否则抛出异常
     *
     * @param array   数组
     * @param message 错误消息
     */
    public static void notEmpty(Object[] array, String message) {
        if (array == null || array.length == 0) {
            throw new com.yisen.common.exception.BusinessException(message);
        }
    }

    /**
     * 断言对象为指定类型，否则抛出异常
     *
     * @param type    类型
     * @param obj     对象
     * @param message 错误消息
     */
    public static void isInstanceOf(Class<?> type, Object obj, String message) {
        notNull(type, "类型不能为空");
        if (!type.isInstance(obj)) {
            throw new com.yisen.common.exception.BusinessException(message);
        }
    }

    /**
     * 断言两个对象相等，否则抛出异常
     *
     * @param obj1    对象1
     * @param obj2    对象2
     * @param message 错误消息
     */
    public static void equals(Object obj1, Object obj2, String message) {
        if (obj1 == null ? obj2 != null : !obj1.equals(obj2)) {
            throw new com.yisen.common.exception.BusinessException(message);
        }
    }

    /**
     * 直接抛出业务异常
     *
     * @param message 错误消息
     */
    public static void fail(String message) {
        throw new com.yisen.common.exception.BusinessException(message);
    }

    /**
     * 直接抛出业务异常（使用枚举）
     *
     * @param responseCode 响应码枚举
     */
    public static void fail(com.yisen.common.enums.ResponseCodeEnum responseCode) {
        throw new com.yisen.common.exception.BusinessException(responseCode);
    }
}

