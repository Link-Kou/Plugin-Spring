package com.linkkou.spring.collectors.sql.change;

import com.linkkou.spring.collectors.sql.SqlQueryFieldChange;

/**
 * @author lk
 * @version 1.0
 * @date 2020/5/22 10:22
 */
public class VarcharChange implements SqlQueryFieldChange {
    /**
     * 改变
     *
     * @param value
     * @return
     */
    @Override
    public Object change(Object value) {
        return String.valueOf(value);
    }
}
