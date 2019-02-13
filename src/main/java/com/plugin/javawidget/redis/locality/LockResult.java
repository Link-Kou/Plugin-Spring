package com.plugin.javawidget.redis.locality;

/**
 * @author lk
 * @version 1.0
 * @date 2019/2/13 22:16
 */
/**
 * 抢锁状态
 *
 * @param <T>
 */
public abstract class LockResult<T> {

    /**
     * 抢到锁
     * @return
     */
    public abstract T success();

    /**
     * 抢锁失败
     * @return
     */
    public abstract T failure();

    /**
     * 解锁是否成功
     * @param status true 成功 false 失败
     * @return
     */
    public abstract T unlock(boolean status);
}