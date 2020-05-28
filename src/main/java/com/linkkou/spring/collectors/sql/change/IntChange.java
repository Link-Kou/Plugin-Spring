package com.linkkou.spring.collectors.sql.change;

import com.linkkou.spring.collectors.sql.SqlQueryFieldChange;

/**
 * @author lk
 * @version 1.0
 * @date 2020/5/22 10:22
 */
public class IntChange implements SqlQueryFieldChange {
    /**
     * 改变
     *
     * @param value
     * @return
     */
    @Override
    public Object change(Object value) {
        try {
            return Integer.parseInt(String.valueOf(value));
        } catch (Exception e) {
            return null;
        }
    }
}
