package com.plugin.javawidget.redis.locality;


import com.plugin.javawidget.redis.RedisClient;
import com.plugin.javawidget.springcontextutil.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Optional;

/**
 * 分布式局部锁,获取失败立即返回
 *
 * @author LK
 * @date 2018/3/31
 * @description
 */
public class RedisLock {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisLock.class);

    private final static String LOCK_SUCCESS = "OK";

    private final static String LOCK_KEY = "REDIS:LOCK:REDISLOCK";

    private final static String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

    private static final Long RELEASE_SUCCESS = 1L;


    /**
     * 分布式局部锁
     *
     * @param key                锁名称
     * @param value              锁值
     * @param functionInitialize 抢锁成功或失败返回
     * @param unlock             解锁失败调用
     * @param <T>
     * @return
     * @deprecated 可以使用但是不推荐
     */
    @Deprecated
    public static <T> T lock(String key, String value, FunctionLockInitialize<T> functionInitialize, FunctionLockInitialize<T> unlock) {
        RedisClient redisClient = (new SpringContextUtil()).getBean(RedisClient.class);
        try {
            LOGGER.debug(String.format("key:%s,value:%s 抢锁中", key, value));
            return functionInitialize.get(LOCK_SUCCESS.equalsIgnoreCase(
                    redisClient.set(
                            Optional.ofNullable(key).orElse(LOCK_KEY),
                            Optional.ofNullable(value).orElse(LOCK_KEY),
                            "NX",
                            "EX",
                            60L)
            ));
        } catch (Exception e) {
            final Long del = redisClient.del(script,
                    Collections.singletonList(Optional.ofNullable(key).orElse(LOCK_KEY)),
                    Collections.singletonList(Optional.ofNullable(value).orElse(LOCK_KEY)));
//            logger.debug(String.format("key:%s,value:%s 解锁失败", key, value));
            return unlock.get(del.equals(RELEASE_SUCCESS));
        }
    }

    /**
     * 分布式局部锁
     *
     * @param key                       锁名称
     * @param value                     锁值
     * @param functionInitialize        抢锁成功
     * @param functionInitializefailure 抢锁失败
     * @param unlock                    解锁失败调用
     * @param <T>
     * @return
     * @deprecated 可以使用但是不推荐
     */
    public static <T> T lock(String key, String value, FunctionLockInitialize<T> functionInitialize, FunctionLockInitialize<T> functionInitializefailure, FunctionLockInitialize<T> unlock) {
        RedisClient redisClient = (new SpringContextUtil()).getBean(RedisClient.class);
        try {
            final boolean b = LOCK_SUCCESS.equalsIgnoreCase(
                    redisClient.set(
                            Optional.ofNullable(key).orElse(LOCK_KEY),
                            Optional.ofNullable(value).orElse(LOCK_KEY),
                            "NX",
                            "EX",
                            60L)
            );
            if (b) {
                LOGGER.debug(String.format("key:%s,value:%s 锁成功", key, value));
                T object = functionInitialize.get(b);
                redisClient.del(script,
                        Collections.singletonList(Optional.ofNullable(key).orElse(LOCK_KEY)),
                        Collections.singletonList(Optional.ofNullable(value).orElse(LOCK_KEY)));
                return object;
            } else {
                LOGGER.debug(String.format("key:%s,value:%s 锁定中", key, value));
                return functionInitializefailure.get(b);
            }
        } catch (Exception e) {
            LOGGER.error("", e);
            final Long del = redisClient.del(script,
                    Collections.singletonList(Optional.ofNullable(key).orElse(LOCK_KEY)),
                    Collections.singletonList(Optional.ofNullable(value).orElse(LOCK_KEY)));
//            logger.debug(String.format("key:%s,value:%s 解锁失败", key, value));
            return unlock.get(del.equals(RELEASE_SUCCESS));
        }
    }

    /**
     * 分布式局部锁
     *
     * @param key        锁名称
     * @param value      锁值
     * @param lockResult 回调结果
     * @param <T>
     * @return
     */
    public static <T> T lock(String key, String value, LockResult<T> lockResult) {
        RedisClient redisClient = (new SpringContextUtil()).getBean(RedisClient.class);
        try {
            final boolean b = LOCK_SUCCESS.equalsIgnoreCase(
                    redisClient.set(
                            Optional.ofNullable(key).orElse(LOCK_KEY),
                            Optional.ofNullable(value).orElse(LOCK_KEY),
                            "NX",
                            "EX",
                            60L)
            );
            if (b) {
                LOGGER.debug(String.format("key:%s,value:%s 锁成功", key, value));
                return lockResult.success();
            } else {
                LOGGER.debug(String.format("key:%s,value:%s 锁定中", key, value));
                return lockResult.failure();
            }
        } catch (Exception e) {
            final Long del = redisClient.del(script,
                    Collections.singletonList(Optional.ofNullable(key).orElse(LOCK_KEY)),
                    Collections.singletonList(Optional.ofNullable(value).orElse(LOCK_KEY)));
            LOGGER.debug(String.format("key:%s,value:%s 解锁失败", key, value));
            return lockResult.unlock(del.equals(RELEASE_SUCCESS));
        }
    }


}
