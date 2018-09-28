package com.plugin.javawidget.redis.locality;

/**
 * @author: LK
 * @date: 2018/3/31
 * @description:
 */
public interface FunctionLockInitialize<T> {

	 T get(boolean rob);
}
