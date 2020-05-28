package com.linkkou.spring.collectors.distinct;

/**
 * @author lk
 * @version 1.0
 * @date 2020/4/15 16:19
 */
public interface FilterFromFunction<T,R> {

    /**
     * 迭代器遍历
     *
     * @param item 被迭代的每一项
     */
    R get(T item);

}
