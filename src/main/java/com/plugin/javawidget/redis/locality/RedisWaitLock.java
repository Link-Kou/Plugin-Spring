package com.plugin.javawidget.redis.locality;

import com.plugin.javawidget.redis.RedisClient;
import com.plugin.javawidget.springcontextutil.SpringContextUtil;

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
                    }
                });
        exec.execute(future);
        //5秒超时
        try {
            return functionInitialize.get(future.get(5000, TimeUnit.MILLISECONDS));
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
            final Long del = redisClient.del(script,
                    Collections.singletonList(Optional.ofNullable(key).orElse(LOCK_KEY)),
                    Collections.singletonList(Optional.ofNullable(value).orElse(LOCK_KEY)));
            return unlock.get(del.equals(RELEASE_SUCCESS));
        }
    }

}
