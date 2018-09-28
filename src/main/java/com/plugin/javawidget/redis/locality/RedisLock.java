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

    private final static String LOCK_SUCCESS = "OK";

    private final static String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

    private static final Long RELEASE_SUCCESS = 1L;

    /**
     * 分布式局部锁
     *
     * @param key   锁名称
     * @param value 锁值
     * @param functionInitialize 抢到锁执行
     * @param unlock             解锁失败
     * @param <T>
     * @return
     */
    public static <T> T lock(String key, String value, FunctionLockInitialize<T> functionInitialize, FunctionLockInitialize<T> unlock) {
        RedisClient redisClient = (new SpringContextUtil()).getBean(RedisClient.class);
        try {
            return functionInitialize.get(LOCK_SUCCESS.equals(redisClient.set(Optional.ofNullable(key).orElse(LOCK_SUCCESS), Optional.ofNullable(key).orElse(LOCK_SUCCESS), "NX", "EX", 60L)));
        }catch (Exception e){
            e.printStackTrace();
            final Long del = redisClient.del(script, Collections.singletonList(key), Collections.singletonList(value));
            return unlock.get(del.equals(RELEASE_SUCCESS));
        }
    }
}
