package com.linkkou.spring.collectots.trees;

import com.linkkou.spring.collectors.trees.Trees;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * @author lk
 * @version 1.0
 * @date 2020/4/15 15:05
 */
public class TreesTest {

    @Test
    public void Test() {
        List<fromTree> sd1 = new ArrayList<>();
        sd1.add(new fromTree().setTitle("1").setId("1").setParentId("").setPreId("0"));
        sd1.add(new fromTree().setTitle("0").setId("0").setParentId("").setPreId("1-1"));
        sd1.add(new fromTree().setTitle("1-1").setId("1-1").setParentId("").setPreId(""));

        sd1.add(new fromTree().setTitle("2").setId("2").setParentId("1").setPreId("2-2"));
        sd1.add(new fromTree().setTitle("2-2").setId("2-2").setParentId("1").setPreId(""));

        sd1.add(new fromTree().setTitle("3-3").setId("3-3").setParentId("2").setPreId("3-2"));
        sd1.add(new fromTree().setTitle("3").setId("3").setParentId("2").setPreId(""));
        sd1.add(new fromTree().setTitle("3-1").setId("3-1").setParentId("2").setPreId("3"));
        sd1.add(new fromTree().setTitle("3-4").setId("3-4").setParentId("2").setPreId("3-3"));
        sd1.add(new fromTree().setTitle("3-2").setId("3-2").setParentId("2").setPreId("3-1"));


        sd1.add(new fromTree().setTitle("4").setId("4").setParentId("3").setPreId(""));


        List<toTree> sd = new ArrayList<>();
        final List<toTree> build = new Trees().from(sd1).to(sd)
                .getId(fromTree::getId)
                .getParentId(fromTree::getParentId)
                .setTranslate(x -> new toTree().setId(x.getId()).setTitle(x.getTitle()).setPreId(x.getPreId()))
                .getSort((x, y) -> {
                    if ("".equals(x.getPreId())) {
                        return -1;
                    }
                    if (Objects.equals(x.getId(), y.getPreId())) {
                        return -1;
                    }
                    if (Objects.equals(x.getPreId(), y.getId())) {
                        return 1;
                    }
                    return x.getId().compareTo(y.getPreId());
                })
                .setChildren((x, y) -> {
                    if (Optional.ofNullable(x.getChildren()).isPresent()) {
                        x.getChildren().addAll(y);
                    } else {
                        x.setChildren(y);
                    }
                    return x.getChildren();
                })
                .build("", (x) -> x.getId());
        System.out.println(1);
    }
}
