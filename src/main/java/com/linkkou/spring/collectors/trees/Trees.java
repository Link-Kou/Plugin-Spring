package com.linkkou.spring.collectors.trees;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 构建树
 * List to Tree
 *
 * @author lk
 * @version 1.0
 * @date 2020/4/15 14:39
 */
public class Trees {

    public <E> Treesto<E> from(Collection<E> list) {
        return new Treesto<E>(list);
    }


    public static class Treesto<T> {

        private Collection<T> from;

        public Treesto(Collection<T> vars) {
            this.from = vars;
        }

        public <R> TreesConfig<T, R> to(Collection<R> to) {
            return new TreesConfig<T, R>(from, to);
        }
    }

    public static class TreesConfig<T, R> {

        private Collection<? extends T> from;
        private Collection<? extends R> to;

        private TreeFromFunction<T, ?> getParentId;
        private TreeFromFunction<T, ?> getId;

        private Map<Object, T> hashMapid = new HashMap<>(16);

        public TreesConfig(Collection<? extends T> from, Collection<? extends R> to) {
            this.from = from;
            this.to = to;
        }

        public <E> TreesConfig<T, R> getId(TreeFromFunction<T, E> id) {
            this.getId = id;
            return this;
        }

        public <E> TreesConfig<T, R> getParentId(TreeFromFunction<T, E> id) {
            this.getParentId = id;
            return this;
        }

        public TreesBuild<R, R> setTranslate(TreeFromFunction<T, R> set) {
            Collection<? extends T> collection = this.from;
            for (T item : collection) {
                if (Optional.ofNullable(item).isPresent()) {
                    final Object id = this.getId.apply(item);
                    this.hashMapid.put(id, item);
                }
            }
            return new TreesBuild(hashMapid, getParentId, set);
        }

    }

    public static class TreesBuild<T, R> {

        private Map<Object, R> hashMapid;

        private TreeFromFunction<R, ?> getParentId;

        private TreeFromFunction<R, R> set;

        private TreeToFunction<R, R> setChildren;

        private Comparator<R> getSort;

        public TreesBuild(Map<Object, R> hashMapid, TreeFromFunction<R, ?> getParentId, TreeFromFunction<R, R> set) {
            this.hashMapid = hashMapid;
            this.getParentId = getParentId;
            this.set = set;
        }

        public TreesBuild<T, R> setChildren(TreeToFunction<R, R> id) {
            this.setChildren = id;
            return this;
        }

        public TreesBuild<T, R> getSort(Comparator<R> c) {
            this.getSort = c;
            return this;
        }

        public <E> List<R> build(String topid, TreeFromFunction<R, E> getid) {
            Map<Object, R> rhashMapid = new HashMap<>(16);
            HashSet<Object> toplist = new HashSet<>();
            List<R> trees = new ArrayList<>();
            this.hashMapid.entrySet().forEach(x -> {
                final R value = x.getValue();
                final R apply = set.apply(value);
                Optional.ofNullable(getid.apply(apply)).ifPresent(o -> rhashMapid.put(o, apply));
            });
            //基于Map递归
            this.hashMapid.entrySet().forEach(x -> {
                final Object apply = this.getParentId.apply(x.getValue());
                //top节点
                if (Objects.equals(apply, topid)) {
                    toplist.add(x.getKey());
                }
                final R pr = rhashMapid.get(apply);
                final R r = rhashMapid.get(x.getKey());
                if (Optional.ofNullable(pr).isPresent()) {
                    final List<R> apply1 = this.setChildren.apply(pr, new ArrayList(Arrays.asList(r)));
                    Collections.sort(apply1, this.getSort);
                }
            });
            toplist.stream().forEach(x -> {
                final R r = rhashMapid.get(x);
                Optional.ofNullable(r).ifPresent(o -> {
                    trees.add(o);
                    Collections.sort(trees, this.getSort);
                });
            });
            return trees;
        }
    }


}
