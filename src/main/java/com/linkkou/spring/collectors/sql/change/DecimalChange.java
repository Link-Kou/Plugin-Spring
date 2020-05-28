package com.linkkou.spring.collectors.sql.change;

import com.linkkou.spring.collectors.sql.SqlQueryFieldChange;

import java.math.BigDecimal;

/**
 * @author lk
 * @version 1.0
 * @date 2020/5/22 10:22
 */
public class DecimalChange implements SqlQueryFieldChange {
    /**
     * 改变
     *
     * @param value
     * @return
     */
    @Override
    public Object change(Object value) {
        BigDecimal recommend = null;
        if (value instanceof String) {
            recommend = new BigDecimal((String) value);
        } else if (value instanceof Double) {
            recommend = BigDecimal.valueOf((Double) value);
        } else if (value instanceof Long) {
            recommend = BigDecimal.valueOf((Long) value);
        }
        return recommend;
    }
}
