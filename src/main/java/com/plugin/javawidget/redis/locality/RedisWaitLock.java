package com.plugin.javawidget.redis.locality;

import com.plugin.javawidget.redis.RedisClient;
import com.plugin.javawidget.springcontextutil.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.*;

/**
 * @author lk
 * @date 2018/9/21 15:34
 */
public class RedisWaitLock {

    private final static String LOCK_SUCCESS = "OK";

    private final static String LOCK_KEY = "REDIS:LOCK:REDISWAITLOCK";

    private final static String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

    private static final Long RELEASE_SUCCESS = 1L;

    private static ExecutorService exec = Executors.newFixedThreadPool(1000);

    private final static Logger LOGGER = LoggerFactory.getLogger(RedisWaitLock.class);

    /**
     * 分布式局部锁-等待锁
     *
     * @param key                锁名称
     * @param value              锁值
     * @param functionInitialize 抢到锁执行
     * @param unlock             没有抢到锁执行
     * @param <T>
     * @return
     */
    public static <T> T lock(String key, String value, FunctionLockInitialize<T> functionInitialize, FunctionLockInitialize<T> unlock) {
        RedisClient redisClient = (new SpringContextUtil()).getBean(RedisClient.class);
        FutureTask<Boolean> future =
                new FutureTask<Boolean>(() -> {
                    //真正的任务在这里执行，这里的返回值类型为String，可以为任意类型
                    while (true) {
                        final boolean b = LOCK_SUCCESS.equalsIgnoreCase(
                                redisClient.set(Optional.ofNullable(key).orElse(LOCK_KEY),
                                        Optional.ofNullable(value).orElse(LOCK_KEY), "NX", "EX", 60L));
                        if (b) {
                            return true;
                        }
                        Thread.sleep(100);
                    }
                });
        exec.execute(future);
        //5秒超时
        try {
            //业务执行
            T obj = functionInitialize.get(future.get(5000, TimeUnit.MILLISECONDS));
            //业务执行完成解锁
            redisClient.del(script,
                    Collections.singletonList(Optional.ofNullable(key).orElse(LOCK_KEY)),
                    Collections.singletonList(Optional.ofNullable(value).orElse(LOCK_KEY)));
            return obj;
        } catch (Exception e) {
            LOGGER.error("", e);
            //业务执行异常解锁
            final Long del = redisClient.del(script,
                    Collections.singletonList(Optional.ofNullable(key).orElse(LOCK_KEY)),
                    Collections.singletonList(Optional.ofNullable(value).orElse(LOCK_KEY)));
            return unlock.get(del.equals(RELEASE_SUCCESS));
        }
    }

    /**
     * 分布式局部锁-等待锁
     *
     * @param key                锁名称
     * @param value              锁值
     * @param waitTime           等待时间(s)
     * @param business 抢到锁执行
     * @param unlock             没有抢到锁执行
     * @param <T>
     * @return
     */
    public static <T> T lock(String key, String value, long waitTime, FunctionLockInitialize<T> business, FunctionLockInitialize<T> timeout, FunctionLockInitialize<T> unlock) {
        RedisClient redisClient = (new SpringContextUtil()).getBean(RedisClient.class);
        try {
            //设置超时结束的时间戳
            long end = System.currentTimeMillis() + waitTime * 1000;
            boolean lockSuccess = false;
            while (System.currentTimeMillis() < end) {
                final boolean b = LOCK_SUCCESS.equalsIgnoreCase(
                        redisClient.set(Optional.ofNullable(key).orElse(LOCK_KEY),
                                Optional.ofNullable(value).orElse(LOCK_KEY), "NX", "EX", 60L));
                if (b) {
                    lockSuccess = b;
                    break;
                }
                //短暂睡眠防止严重阻塞
                Thread.sleep(100);
            }
            T result;
            if(lockSuccess){
                //业务执行
                result = business.get(lockSuccess);
                //业务执行完成解锁
                redisClient.del(script,
                        Collections.singletonList(Optional.ofNullable(key).orElse(LOCK_KEY)),
                        Collections.singletonList(Optional.ofNullable(value).orElse(LOCK_KEY)));
            } else {
                result = timeout.get(lockSuccess);
            }
            return result;
        } catch (Exception e) {
            LOGGER.error("", e);
            //业务执行异常解锁
            final Long del = redisClient.del(script,
                    Collections.singletonList(Optional.ofNullable(key).orElse(LOCK_KEY)),
                    Collections.singletonList(Optional.ofNullable(value).orElse(LOCK_KEY)));
            return unlock.get(del.equals(RELEASE_SUCCESS));
        }
    }

}
