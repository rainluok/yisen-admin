package com.yisen.common.aspect;

import com.yisen.common.annotation.Sensitive;
import com.yisen.common.enums.SensitiveType;
import com.yisen.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

/**
 * 敏感数据脱敏切面
 * 使用 AOP 实现敏感数据脱敏
 * 对返回值进行脱敏处理
 *
 * @author rainluo
 * @date 2025-11-14
 */
@Slf4j
@Aspect
@Component
@Order(20)
public class SensitiveDataAspect {

    /**
     * 环绕通知：对所有返回值进行脱敏处理
     * 只拦截带有 EnableSensitive 注解的接口或类
     */
    @Around("@within(com.yisen.common.annotation.EnableSensitive) || @annotation(com.yisen.common.annotation.EnableSensitive)")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // 执行方法
        Object result = joinPoint.proceed();

        // 如果返回值为空，直接返回
        if (result == null) {
            return null;
        }

        try {
            // 对返回值进行脱敏处理
            // 创建基于引用相等的 visited 集合
            Set<Object> visited = Collections.newSetFromMap(new IdentityHashMap<>());
            maskSensitiveData(result, visited);
        } catch (Exception e) {
            log.error("敏感数据脱敏失败", e);
        }
        return result;
    }

    /**
     * 递归处理敏感数据
     */
    private void maskSensitiveData(Object obj, Set<Object> visited) throws IllegalAccessException {
        if (obj == null) {
            return;
        }

        // 🔒 防循环引用：如果已处理过，立即退出
        if (!visited.add(obj)) {
            return; // 这是防止无限递归的核心！
        }

        Class<?> clazz = obj.getClass();

        // 基本类型、String、LocalDateTime 等无需递归
        if (clazz.isPrimitive() || clazz.getName().startsWith("java.lang") || clazz.getName().startsWith("java.time")) {
            return;
        }

        // 处理 Result 对象
        if (obj instanceof Result) {
            Result<?> resultObj = (Result<?>) obj;
            if (resultObj.getData() != null) {
                maskSensitiveData(resultObj.getData(), visited);
            }
            return;
        }

        // 处理集合
        if (obj instanceof Collection) {
            Collection<?> collection = (Collection<?>) obj;
            for (Object item : collection) {
                maskSensitiveData(item, visited);
            }
            return;
        }

        // 👇 处理普通对象字段
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);

            Object value = field.get(obj);
            if (value == null) continue;

            Sensitive sensitive = field.getAnnotation(Sensitive.class);
            if (sensitive != null && value instanceof String) {
                // 脱敏字符串
                field.set(obj, maskValue((String) value, sensitive.type()));
            } else {
                // 即使没注解，也要递归进去（因为 value 可能是对象）
                // ⚠️ 下一层递归开头会检查 visited，防止循环！
                maskSensitiveData(value, visited);
            }
        }
    }

    /**
     * 根据脱敏类型对值进行脱敏
     */
    private String maskValue(String value, SensitiveType type) {
        if (value == null || value.isEmpty()) {
            return value;
        }

        return switch (type) {
            case MOBILE -> {
                // 手机号：保留前3位和后4位
                yield maskMobile(value);
            }
            case ID_CARD -> {
                // 身份证：保留前6位和后4位
                yield maskIdCard(value);
            }
            case EMAIL -> {
                // 邮箱：保留邮箱前缀第一个字符和@后面的部分
                yield maskEmail(value);
            }
            case BANK_CARD -> {
                // 银行卡：保留前4位和后4位
                yield maskBankCard(value);
            }
            case PASSWORD -> {
                // 密码：全部脱敏
                yield "******";
            }
            case NAME -> {
                // 姓名：保留姓，名字脱敏
                yield maskName(value);
            }
            case ADDRESS -> {
                // 地址：保留前6个字符
                yield maskAddress(value);
            }
            default -> {
                log.error("未知的脱敏类型：{}", type);
                yield value;
            }
        };
    }

    /**
     * 手机号脱敏：138****5678
     */
    private String maskMobile(String mobile) {
        if (mobile == null || mobile.length() != 11) {
            return mobile;
        }
        return mobile.substring(0, 3) + "****" + mobile.substring(7);
    }

    /**
     * 身份证脱敏：110101********1234
     */
    private String maskIdCard(String idCard) {
        if (idCard == null || idCard.length() < 10) {
            return idCard;
        }
        return idCard.substring(0, 6) + "********" + idCard.substring(idCard.length() - 4);
    }

    /**
     * 邮箱脱敏：a****@qq.com
     */
    private String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return email;
        }
        String[] parts = email.split("@");
        if (parts[0].length() <= 1) {
            return email;
        }
        return parts[0].charAt(0) + "****@" + parts[1];
    }

    /**
     * 银行卡脱敏：6222****5678
     */
    private String maskBankCard(String bankCard) {
        if (bankCard == null || bankCard.length() < 8) {
            return bankCard;
        }
        return bankCard.substring(0, 4) + "****" + bankCard.substring(bankCard.length() - 4);
    }

    /**
     * 姓名脱敏：张*、王**
     */
    private String maskName(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        if (name.length() == 1) {
            return name;
        }
        StringBuilder masked = new StringBuilder();
        masked.append(name.charAt(0));
        for (int i = 1; i < name.length(); i++) {
            masked.append("*");
        }
        return masked.toString();
    }

    /**
     * 地址脱敏：保留前6个字符
     */
    private String maskAddress(String address) {
        if (address == null || address.length() <= 6) {
            return address;
        }
        return address.substring(0, 6) + "****";
    }
}

