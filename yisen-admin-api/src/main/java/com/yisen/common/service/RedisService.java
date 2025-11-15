package com.yisen.common.service;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Redis服务封装
 * 实现常用Redis操作
 *
 * @author rainluo
 * @version 1.0
 * @date 2025/11/14 10:27
 */
@Service
public class RedisService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 设置缓存，默认无限期
     *
     * @param key   缓存key
     * @param value 缓存值
     */
    public void set(String key, Object value) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        ops.set(key, value);
    }

    /**
     * 设置缓存，带过期时间
     *
     * @param key     缓存key
     * @param value   缓存值
     * @param timeout 过期时间（秒）
     */
    public void set(String key, Object value, long timeout) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        ops.set(key, value, timeout, TimeUnit.SECONDS);
    }

    public void set(String key, Object value, long timeout, TimeUnit timeUnit) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        ops.set(key, value, timeout, timeUnit);
    }

    /**
     * 根据key获取缓存
     *
     * @param key 缓存key
     * @return 缓存值
     */
    public Object get(String key) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        return ops.get(key);
    }


    /**
     * 判断缓存是否存在
     *
     * @param key 缓存key
     * @return 是否存在
     */
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 删除缓存
     *
     * @param key 缓存key
     * @return 是否成功删除
     */
    public boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 设置缓存过期时间
     *
     * @param key     缓存key
     * @param timeout 过期时间（秒）
     * @return 是否设置成功
     */
    public boolean expire(String key, long timeout) {
        return redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }


    /**
     * 设置缓存过期时间
     *
     * @param key      缓存key
     * @param timeout  过期时间
     * @param timeUnit 时间单位
     * @return 是否设置成功
     */
    public boolean expire(String key, long timeout, TimeUnit timeUnit) {
        return redisTemplate.expire(key, timeout, timeUnit);
    }

    /**
     * 获取缓存剩余过期时间
     *
     * @param key 缓存key
     * @return 剩余时间（秒），-1为永久有效，-2为不存在
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }


    public Long increment(String key) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        return ops.increment(key);
    }
}
