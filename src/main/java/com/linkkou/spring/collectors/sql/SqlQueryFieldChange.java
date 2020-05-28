package com.linkkou.spring.collectors.sql;

/**
 * @author lk
 * @version 1.0
 * @date 2020/5/22 08:36
 */
public interface SqlQueryFieldChange {

    /**
     * 改变
     * @param value
     * @return
     */
    Object change(Object value);
}
