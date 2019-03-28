package com.example.demo.utils;

import org.springframework.data.redis.core.*;

import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class RedisTemplateUtil {

    private static final RedisTemplate<String, Object> redisTemplate = SpringContext.getBean("redisTemplate");

    private static final StringRedisTemplate stringRedisTemplate = SpringContext.getBean("stringRedisTemplate");

    public static ValueOperations<String, ?> valueOps(){
        return redisTemplate.opsForValue();
    }

    public static BoundValueOperations<String, ?> boundValueOps(String key){
        return redisTemplate.boundValueOps(key);
    }

    public static HashOperations<String, String, ?> hashOps(){
        return redisTemplate.opsForHash();
    }

    public static BoundHashOperations<String, String, ?> boundHashOps(String key){
        return redisTemplate.boundHashOps(key);
    }

    public static ListOperations<String, ?> listOps(){
        return redisTemplate.opsForList();
    }

    public static BoundListOperations<String, ?> boundListOps(String key){
        return redisTemplate.boundListOps(key);
    }

    public static SetOperations<String, ?> getSetOps(){
        return redisTemplate.opsForSet();
    }

    public static BoundSetOperations<String, ?> getSetOps(String key){
        return redisTemplate.boundSetOps(key);
    }

    /**
     * 获取hashMap值
     */
    public static Object hashGet(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * 新增hashMap值
     */
    public static void hashSet(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * 删除hashMap值
     */
    public static void hashDel(String key, String hashKey) {
        redisTemplate.opsForHash().delete(key, hashKey);
    }

    /**
     * 设置缓存内容及过期时间(秒)
     */
    public static void set(String key, Object value, long expire) {
        redisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
    }

    /**
     * 设置缓存内容及过期时间(秒)
     */
    public static void set(String key, String value, long expire) {
        stringRedisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
    }

    /**
     * 设置缓存内容（无限期，慎用）
     */
    public static void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置缓存内容（无限期，慎用）
     */
    public static void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    /**
     * 获取缓存内容
     */
    public static <T> T get(String key) {
        return (T) redisTemplate.opsForValue().get(key);
    }

    /**
     * 获取缓存内容
     */
    public static String getString(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 删除Key
     */
    public static Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 批量删除Key
     */
    public static Long delete(Collection<String> keys) {
        return redisTemplate.delete(keys);
    }

    /**
     * 模糊删除Key
     */
    public static Long deleteByKeyPattern(String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        return redisTemplate.delete(keys);
    }

    /**
     * 检查key是否存在
     */
    public static Boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 设置key过期时间(秒)
     */
    public static Boolean expire(String key, long expire) {
        if (expire <= 0) {
            return Boolean.FALSE;
        }
        return redisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }

    /**
     * 设置key定时过期时间
     */
    public static Boolean expireAt(String key, Date unixTime) {
        if (unixTime == null) {
            return Boolean.FALSE;
        }
        return redisTemplate.expireAt(key, unixTime);
    }

    /**
     * 原子计数
     */
    public static Long atomicLong(final String key) {
        return redisTemplate.opsForValue().increment(key, 1L);
    }
}
