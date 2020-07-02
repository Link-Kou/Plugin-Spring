package com.linkkou.spring.collectots.trees;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author lk
 * @version 1.0
 * @date 2020/4/15 15:26
 */
@Data
@Accessors(chain = true)
public class toTree {
    private String id;

    private String title;

    private String preId;

    private List<toTree> children;

}
