package com.yisen.common.aspect;

import com.yisen.common.annotation.Sensitive;
import com.yisen.common.enums.SensitiveType;
import com.yisen.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * 敏感数据脱敏切面
 * 使用 AOP 实现敏感数据脱敏，替代 SensitiveDataFilter
 * 对返回值进行脱敏处理
 *
 * @author rainluo
 * @date 2025-11-14
 */
@Slf4j
@Aspect
@Component
public class SensitiveDataAspect {

    /**
     * 环绕通知：对所有 Controller 的返回值进行脱敏处理
     */
    @Around("execution(* com.yisen.module..controller.*.*(..))")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // 执行方法
        Object result = joinPoint.proceed();

        // 如果返回值为空，直接返回
        if (result == null) {
            return null;
        }

        try {
            // 对返回值进行脱敏处理
            maskSensitiveData(result);
        } catch (Exception e) {
            log.error("敏感数据脱敏失败", e);
        }

        return result;
    }

    /**
     * 递归处理敏感数据
     */
    private void maskSensitiveData(Object obj) throws IllegalAccessException {
        if (obj == null) {
            return;
        }

        Class<?> clazz = obj.getClass();

        // 处理基本类型、包装类、String 等
        if (clazz.isPrimitive() || clazz.getName().startsWith("java.lang")) {
            return;
        }

        // 处理 Result 对象
        if (obj instanceof Result) {
            Result<?> resultObj = (Result<?>) obj;
            if (resultObj.getData() != null) {
                maskSensitiveData(resultObj.getData());
            }
            return;
        }

        // 处理集合
        if (obj instanceof Collection) {
            Collection<?> collection = (Collection<?>) obj;
            for (Object item : collection) {
                maskSensitiveData(item);
            }
            return;
        }

        // 处理普通对象
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);

            // 检查字段是否有 @Sensitive 注解
            Sensitive sensitive = field.getAnnotation(Sensitive.class);
            if (sensitive != null) {
                Object value = field.get(obj);
                if (value instanceof String) {
                    String maskedValue = maskValue((String) value, sensitive.type());
                    field.set(obj, maskedValue);
                }
            } else {
                // 递归处理嵌套对象
                Object fieldValue = field.get(obj);
                if (fieldValue != null) {
                    maskSensitiveData(fieldValue);
                }
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

        switch (type) {
            case MOBILE:
                // 手机号：保留前3位和后4位
                return maskMobile(value);
            case ID_CARD:
                // 身份证：保留前6位和后4位
                return maskIdCard(value);
            case EMAIL:
                // 邮箱：保留邮箱前缀第一个字符和@后面的部分
                return maskEmail(value);
            case BANK_CARD:
                // 银行卡：保留前4位和后4位
                return maskBankCard(value);
            case PASSWORD:
                // 密码：全部脱敏
                return "******";
            case NAME:
                // 姓名：保留姓，名字脱敏
                return maskName(value);
            case ADDRESS:
                // 地址：保留前6个字符
                return maskAddress(value);
            default:
                return value;
        }
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

