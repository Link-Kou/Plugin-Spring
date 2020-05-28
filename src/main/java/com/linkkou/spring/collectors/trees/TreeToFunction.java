package com.linkkou.spring.collectors.trees;

import java.util.List;

/**
 * @author lk
 * @version 1.0
 * @date 2020/4/15 16:19
 */
public interface TreeToFunction<T, R> {

    /**
     * 迭代器遍历
     *
     * @param item1 被迭代的每一项
     */
    List<T> apply(T item1, List<R> item2);

}
