package com.linkkou.spring.collectots.trees;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author lk
 * @version 1.0
 * @date 2020/4/15 15:26
 */
@Data
@Accessors(chain = true)
public class fromTree {
    private String id;

    private String parentId;

    private String preId;

    private String title;
}
