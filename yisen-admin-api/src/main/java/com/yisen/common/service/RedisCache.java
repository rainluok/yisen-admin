package com.yisen.common.service;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
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
public class RedisCache {

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 设置缓存，默认无限期
     *
     * @param key   缓存key
     * @param value 缓存值
     */
    public <T> void set(String key, T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置缓存，带过期时间
     *
     * @param key     缓存key
     * @param value   缓存值
     * @param timeout 过期时间（秒）
     */
    public <T> void set(String key, T value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    public <T> void set(String key, T value, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 根据key获取缓存
     *
     * @param key 缓存key
     * @return 缓存值
     */
    public <T> T get(String key) {
        return (T) redisTemplate.opsForValue().get(key);
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


    /**
     * 仅当 key 不存在时设置（防重、分布式锁）
     *
     * @return true=设置成功，false=key已存在
     */
    public boolean setIfAbsent(String key, String value, long expire) {
        Boolean result = redisTemplate.opsForValue().setIfAbsent(key, value, expire, TimeUnit.SECONDS);
        return Boolean.TRUE.equals(result);
    }

    /**
     * 自增（原子）
     *
     * @param delta 步长（可为负数）
     * @return 自增后的新值
     */
    public Long increment(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 自增 1
     */
    public Long increment(String key) {
        return increment(key, 1);
    }
}
