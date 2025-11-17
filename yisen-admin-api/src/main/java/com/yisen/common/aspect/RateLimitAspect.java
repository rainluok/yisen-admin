package com.yisen.common.aspect;

import com.yisen.common.annotation.RateLimit;
import com.yisen.common.enums.ResponseCodeEnum;
import com.yisen.common.exception.BusinessException;
import com.yisen.common.util.IpUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Collections;

/**
 * 接口限流切面（基于令牌桶算法 + Lua 脚本）
 * 使用 AOP 实现接口限流，替代 RateLimitInterceptor
 *
 * @author rainluo
 * @date 2025-11-14
 */
@Slf4j
@Aspect
@Component
@Order(40)
public class RateLimitAspect {

    private static final String RATE_LIMIT_KEY_PREFIX = "rate:limit:";
    // Lua 脚本实现令牌桶算法
    private static final String LUA_SCRIPT =
            "local key = KEYS[1]\n" +
                    "local limit = tonumber(ARGV[1])\n" +
                    "local current = tonumber(redis.call('get', key) or '0')\n" +
                    "if current + 1 > limit then\n" +
                    "    return 0\n" +
                    "else\n" +
                    "    redis.call('INCRBY', key, 1)\n" +
                    "    redis.call('expire', key, 1)\n" +
                    "    return 1\n" +
                    "end";
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 前置通知：检查限流
     */
    @Before("@annotation(rateLimit)")
    public void doBefore(JoinPoint joinPoint, RateLimit rateLimit) {
        // 获取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            log.warn("无法获取请求上下文，跳过限流检查");
            return;
        }
        HttpServletRequest request = attributes.getRequest();

        // 获取方法信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 生成限流键
        String key = generateKey(request, method, rateLimit);
        int limit = rateLimit.limit();

        // 执行 Lua 脚本
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(LUA_SCRIPT, Long.class);
        Long result = redisTemplate.execute(redisScript, Collections.singletonList(key), limit);

        if (result == null || result == 0) {
            log.warn("接口限流触发: {} 限制 {}/秒", key, limit);
            throw new BusinessException(ResponseCodeEnum.RATE_LIMIT_EXCEEDED);
        }

        log.debug("限流检查通过: {}", key);
    }

    /**
     * 生成限流键
     */
    private String generateKey(HttpServletRequest request, Method method, RateLimit rateLimit) {
        String methodSignature = method.getDeclaringClass().getName() + "." + method.getName();

        if (rateLimit.limitType() == RateLimit.LimitType.IP) {
            // 基于 IP 限流
            String ip = IpUtil.getIpAddr(request);
            return RATE_LIMIT_KEY_PREFIX + "ip:" + ip + ":" + methodSignature;
        } else {
            // 基于用户限流（需要从Token中获取用户ID）
            String userToken = request.getHeader("Authorization");
            return RATE_LIMIT_KEY_PREFIX + "user:" + userToken + ":" + methodSignature;
        }
    }
}

