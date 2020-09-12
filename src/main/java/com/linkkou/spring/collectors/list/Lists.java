package com.linkkou.spring.collectors.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author lk
 * @version 1.0
 * @date 2020/5/15 13:46
 */
public class Lists {
    /**
     * 创建 List
     *
     * @param of  参数
     * @param <T> 泛型
     * @return <T> 泛型
     */
    @SafeVarargs
    public static <T> List<T> of(T... of) {
        return Stream.of(of).collect(Collectors.toList());
    }

    /**
     * 创建 ArrayList
     *
     * @param of  参数
     * @param <T> 泛型
     * @return <T> 泛型
     */
    @SafeVarargs
    public static <T> ArrayList<T> ofArrayList(T... of) {
        ArrayList<T> s = new ArrayList<T>();
        Collections.addAll(s, of);
        return s;
    }

    /**
     * 多个参数判断是否为空
     *
     * @param of 对象
     * @param <T> 泛型
     * @return boolean
     */
    @SafeVarargs
    public static <T> boolean ofNullable(T... of) {
        final List<Boolean> collect = Stream.of(of).map(x -> {
            return Optional.ofNullable(x).isPresent();
        }).filter(x -> !x).collect(Collectors.toList());
        return collect.size() <= 0;
    }
}
