package com.linkkou.spring.collectors.trees;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * List to Tree
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

        private Map<Object, T> hashMapid = new ConcurrentHashMap<>(16);
        private Map<Object, List<T>> hashMapParentid = new ConcurrentHashMap<>(16);

        public TreesConfig(Collection<? extends T> from, Collection<? extends R> to) {
            this.from = from;
            this.to = to;
        }

        public <E> TreesConfig<T, R> getId(TreeFromFunction<T, E> id) {
            Collection<? extends T> collection = this.from;
            for (T item : collection) {
                if (Optional.ofNullable(item).isPresent()) {
                    final E apply = id.apply(item);
                    this.hashMapid.put(apply, item);
                }
            }
            return this;
        }

        public <E> TreesConfig<T, R> getParentId(TreeFromFunction<T, E> id) {
            this.getParentId = id;
            Collection<? extends T> collection = this.from;
            for (T item : collection) {
                if (Optional.ofNullable(item).isPresent()) {
                    final E apply = id.apply(item);
                    List<T> ts;
                    if (this.hashMapParentid.containsKey(apply)) {
                        ts = this.hashMapParentid.get(apply);
                        ts.add(item);
                    } else {
                        ts = new ArrayList<>();
                        ts.add(item);
                    }
                    this.hashMapParentid.put(apply, ts);
                }
            }
            return this;
        }

        public TreesBuild<R, R> setTranslate(TreeFromFunction<T, R> set) {
            Map<Object, R> rhashMapid = new ConcurrentHashMap<>(16);
            Map<Object, List<R>> rhashMapParentid = new ConcurrentHashMap<>(16);

            hashMapid.entrySet().forEach(x -> {
                final T value = x.getValue();
                final List<T> t = hashMapParentid.get(x.getKey());
                List<R> collect;
                if (Optional.ofNullable(t).isPresent()) {
                    collect = t.stream().map((k) -> set.apply(k)).collect(Collectors.toList());
                } else {
                    collect = new ArrayList<>();
                }
                final R apply = set.apply(value);
                rhashMapid.put(x.getKey(), apply);
                rhashMapParentid.put(x.getKey(), collect);
            });
            return new TreesBuild(hashMapid, this.getParentId, rhashMapid, rhashMapParentid);
        }

    }

    public static class TreesBuild<T, R> {

        private Map<Object, R> oldhashMapid;

        private TreeFromFunction<R, ?> getParentId;

        private Map<Object, T> hashMapid;

        private Map<Object, List<T>> hashMapParentid;

        private TreeToFunction<T, T> setChildren;

        private List<T> trees = new ArrayList<>();

        public TreesBuild(Map<Object, R> oldhashMapid, TreeFromFunction<R, ?> getParentId, Map<Object, T> hashMapid, Map<Object, List<T>> hashMapParentid) {
            this.oldhashMapid = oldhashMapid;
            this.getParentId = getParentId;
            this.hashMapid = hashMapid;
            this.hashMapParentid = hashMapParentid;
        }

        public TreesBuild<T, R> setChildren(TreeToFunction<T, T> id) {
            this.setChildren = id;
            return this;
        }

        public <E> List<T> build(String topid, TreeFromFunction<T, E> getid) {
            oldhashMapid.entrySet().forEach(x -> {
                if (Objects.equals(this.getParentId.apply(x.getValue()), topid)) {
                    final T t = hashMapid.get(x.getKey());
                    final List<T> ts = hashMapParentid.get(x.getKey());
                    ts.forEach(fts -> {
                        final List<T> ts1 = hashMapParentid.get(getid.apply(fts));
                        this.setChildren.apply(fts, ts1);
                    });
                    this.setChildren.apply(t, ts);
                    trees.add(t);
                }
                //final T t = hashMapid.get(x.getKey());
                //trees.add(this.setChildren.apply(t, ts));
            });
            /*hashMapid.entrySet().forEach(x -> {
                final List<T> t = hashMapParentid.get(x.getKey());
                trees.add(this.setChildren.apply(x.getValue(), t));
            });*/
            return this.trees;
        }
    }


}
