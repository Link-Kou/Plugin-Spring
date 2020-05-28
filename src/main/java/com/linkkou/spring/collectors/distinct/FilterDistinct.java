package com.linkkou.spring.collectors.distinct;

import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Predicate;

/**
 * 过滤去重复
 *
 * @author lk
 * @version 1.0
 * @date 2020/4/16 20:45
 */
public class FilterDistinct {

    /**
     * 返回表达式
     *
     * @param other 接口
     * @param <T>   输入
     * @param <R>   输出
     * @return 接口 {@link Predicate}
     */
    public static <T, R> Predicate<T> run(FilterFromFunction<T, R> other) {
        HashSet<R> hashSet = new HashSet<>();
        return p -> {
            final R r = other.get(p);
            if (hashSet.contains(r)) {
                return false;
            }
            hashSet.add(r);
            return true;
        };
    }


    public static <R1 extends List, R2 extends List> MultiplePair<R1, R2> multiple(R1 r1, R2 r2) {
        HashSet<Object> hashSet1 = new HashSet<>();
        HashSet<Object> hashSet2 = new HashSet<>();
        List<Object> list1 = new ArrayList<>();
        List<Object> list2 = new ArrayList<>();
        return new MultiplePair<R1, R2>(hashSet1, hashSet2, list1, list2);
    }


    public static class MultiplePair<R1, R2> {
        private final HashSet<Object> hashSet1;
        private final HashSet<Object> hashSet2;
        private final List<Object> list1;
        private final List<Object> list2;

        public MultiplePair(HashSet<Object> hashSet1, HashSet<Object> hashSet2, List<Object> list1, List<Object> list2) {
            this.hashSet1 = hashSet1;
            this.hashSet2 = hashSet2;
            this.list1 = list1;
            this.list2 = list2;
        }

        public <T, E1, E2> MultiplePairMap<T, E1, E2> filter(T val, FilterFromFunction<T, E1> other1, FilterFromFunction<T, E2> other2) {
            return new MultiplePairMap<T, E1, E2>(val, other1, other2, hashSet1, hashSet2, list1, list2);
        }


        public Pair<R1, R2> get() {
            return (Pair<R1, R2>) Pair.with(list1, list2);
        }


        public static class MultiplePairMap<T, R1, R2> {

            private final HashSet<Object> hashSet1;
            private final HashSet<Object> hashSet2;
            private final List<Object> list1;
            private final List<Object> list2;

            private T val;
            private FilterFromFunction<T, R1> other1;
            private FilterFromFunction<T, R2> other2;

            public MultiplePairMap(T val, FilterFromFunction<T, R1> other1, FilterFromFunction<T, R2> other2, HashSet<Object> hashSet1, HashSet<Object> hashSet2, List<Object> list1, List<Object> list2) {
                this.val = val;
                this.other1 = other1;
                this.other2 = other2;
                this.hashSet1 = hashSet1;
                this.hashSet2 = hashSet2;
                this.list1 = list1;
                this.list2 = list2;
            }

            public <E1, E2> void map(FilterFromFunction<T, E1> mother1, FilterFromFunction<T, E2> mother2) {
                final R1 e1 = this.other1.get(this.val);
                final R2 e2 = this.other2.get(this.val);
                set(this.val, mother1, e1, mother2, e2);
            }

            private <T, E1, R1, E2, R2> void set(T val, FilterFromFunction<T, E1> mother1, R1 r1, FilterFromFunction<T, E2> mother2, R2 r2) {
                final HashSet<Object> o1 = this.hashSet1;
                final HashSet<Object> o2 = this.hashSet2;
                if (!o1.contains(r1)) {
                    o1.add(r1);
                    final E1 e1 = mother1.get(val);
                    this.list1.add(e1);
                }
                if (!o2.contains(r2)) {
                    o2.add(r2);
                    final E2 e2 = mother2.get(val);
                    this.list2.add(e2);
                }
            }
        }


    }

}
