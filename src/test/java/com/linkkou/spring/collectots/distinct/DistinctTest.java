package com.linkkou.spring.collectots.distinct;

import com.linkkou.spring.collectors.distinct.FilterDistinct;
import com.linkkou.spring.collectors.trees.Trees;
import com.linkkou.spring.collectots.trees.fromTree;
import com.linkkou.spring.collectots.trees.toTree;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author lk
 * @version 1.0
 * @date 2020/4/15 15:05
 */
public class DistinctTest {

    @Test
    public void Test() {
        List<String> v = new ArrayList<>();
        v.add("1");
        v.add("1");
        v.add("2");
        v.add("3");
        final List<String> collect = v.stream().filter(FilterDistinct.run(x -> x)).collect(Collectors.toList());
        System.out.println("");
    }
}
