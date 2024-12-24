package com.example.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // 设置值
    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    // 设置值并设置过期时间
    public void set(String key, String value, long timeout, TimeUnit timeUnit) {
        stringRedisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    // 获取值
    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    // 删除值
    public void delete(String key) {
        stringRedisTemplate.delete(key);
    }

    // 设置对象
    public void setObject(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    // 设置对象并设置过期时间
    public void setObject(String key, Object value, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    // 获取对象
    public Object getObject(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // 设置过期时间
    public boolean expire(String key, long timeout, TimeUnit timeUnit) {
        return redisTemplate.expire(key, timeout, timeUnit);
    }

    // 获取剩余过期时间
    public long getExpire(String key, TimeUnit timeUnit) {
        return redisTemplate.getExpire(key, timeUnit);
    }
}