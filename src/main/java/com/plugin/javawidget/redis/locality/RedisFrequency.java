package com.plugin.javawidget.redis.locality;

import com.plugin.javawidget.redis.RedisClient;
import com.plugin.javawidget.springcontextutil.SpringContextUtil;

import java.util.Optional;

/**
 * 分布式次数限制
 *
 * @author lk
 * @date 2018/10/3 14:04
 */
public class RedisFrequency {

    private final static String LOCK_SUCCESS = "OK";

    private final static String LOCK_KEY = "REDIS:LOCK:REDISFREQUENCY";

    private static final Long RELEASE_SUCCESS = 1L;

    /**
     * 分布式次数限制
     *
     * @param key                锁名称
     * @param number             次数
     * @param functionInitialize 抢到锁执行
     * @param <T>
     * @return
     */
    public static <T> T lock(String key, int number, FunctionLockInitialize<T> functionInitialize) {
        RedisClient redisClient = (new SpringContextUtil()).getBean(RedisClient.class);
        try {
            final boolean equals = LOCK_SUCCESS.equals(
                    redisClient.set(Optional.ofNullable(key).orElse(LOCK_KEY),
                            "0", "NX", "EX", 60L)
            );
            return functionInitialize.get(
                    equals ? number >= redisClient.incr(Optional.ofNullable(key).orElse(LOCK_KEY)) : equals
            );
        } catch (Exception e) {
            throw e;
        }
    }


    /**
     * 分布式时间限制
     *
     * @param key                锁名称
     * @param time               时间(秒)
     * @param functionInitialize 抢到锁执行
     * @param <T>
     * @return
     */
    public static <T> T lock(String key, long time, FunctionLockInitialize<T> functionInitialize) {
        RedisClient redisClient = (new SpringContextUtil()).getBean(RedisClient.class);
        try {
            final boolean equals = LOCK_SUCCESS.equals(
                    redisClient.set(Optional.ofNullable(key).orElse(LOCK_KEY),
                            "0", "NX", "EX", time)
            );
            return functionInitialize.get(equals);
        } catch (Exception e) {
            throw e;
        }
    }
}
