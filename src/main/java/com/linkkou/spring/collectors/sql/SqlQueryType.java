package com.linkkou.spring.collectors.sql;

/**
 * @author lk
 * @version 1.0
 * @date 2020/5/22 10:01
 */
public enum SqlQueryType {
    /**
     * 文本
     */
    VARCHAR(1),
    /**
     * 浮点
     */
    DECIMAL(2),
    /**
     * 整数
     */
    INT(3);

    public final int TYPE_CODE;

    SqlQueryType(int typecode) {
        this.TYPE_CODE = typecode;
    }
}
