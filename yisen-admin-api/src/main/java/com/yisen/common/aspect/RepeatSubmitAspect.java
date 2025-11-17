package com.yisen.common.aspect;

import com.yisen.common.annotation.RepeatSubmit;
import com.yisen.common.enums.ResponseCodeEnum;
import com.yisen.common.exception.BusinessException;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 防重复提交切面（基于分布式锁）
 * 使用 AOP 实现防重复提交
 *
 * @author rainluo
 * @date 2025-11-14
 */
@Slf4j
@Aspect
@Component
@Order(50)
public class RepeatSubmitAspect {

    private static final String REPEAT_SUBMIT_KEY_PREFIX = "repeat:submit:";
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 前置通知：检查是否重复提交
     */
    @Before("@annotation(repeatSubmit)")
    public void doBefore(JoinPoint joinPoint, RepeatSubmit repeatSubmit) {
        // 获取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            log.warn("无法获取请求上下文，跳过防重复提交检查");
            return;
        }
        HttpServletRequest request = attributes.getRequest();

        // 获取方法信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 生成唯一键：用户标识 + 方法签名 + 参数哈希
        String key = generateKey(request, method, joinPoint.getArgs());
        long interval = repeatSubmit.interval();

        // 尝试设置键，如果键已存在则说明重复提交
        Boolean success = redisTemplate.opsForValue().setIfAbsent(
                key, System.currentTimeMillis(), interval, TimeUnit.SECONDS
        );

        if (Boolean.FALSE.equals(success)) {
            log.warn("检测到重复提交: {}", key);
            throw new BusinessException(ResponseCodeEnum.REPEAT_SUBMIT);
        }

        log.debug("防重复提交检查通过: {}", key);
    }

    /**
     * 生成唯一键
     */
    private String generateKey(HttpServletRequest request, Method method, Object[] args) {
        // 获取用户标识（可以从Token或Session中获取）
        String userToken = request.getHeader("Authorization");
        if (userToken == null) {
            userToken = request.getSession().getId();
        }

        // 方法签名
        String methodSignature = method.getDeclaringClass().getName() + "." + method.getName();

        // 计算参数哈希（简化处理）
        int argsHash = 0;
        if (args != null && args.length > 0) {
            for (Object arg : args) {
                if (arg != null) {
                    // 跳过 HttpServletRequest 等对象
                    String argClassName = arg.getClass().getName();
                    if (!argClassName.contains("HttpServlet") && !argClassName.contains("MultipartFile")) {
                        argsHash += arg.hashCode();
                    }
                }
            }
        }

        return REPEAT_SUBMIT_KEY_PREFIX + userToken + ":" + methodSignature + ":" + argsHash;
    }
}

