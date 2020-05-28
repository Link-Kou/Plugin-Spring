package com.linkkou.spring.collectors.sql;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author lk
 * @version 1.0
 * @date 2020/5/21 17:53
 */
@Data
@Accessors(chain = true)
public class SqlQueryProvider {
    private String id;
    /**
     * expression group
     */
    private SqlQueryProviderTypeEnum type;
    /**
     * AND OR
     */
    private SqlQueryProviderLinkEnum link;
    /**
     * 子对象
     */
    private List<SqlQueryProvider> children;
    /**
     * 扩展值
     */
    private Extend extend;

    @Data
    @Accessors(chain = true)
    public static class Extend {
        /**
         * 搜索字段
         */
        private String field;
        /**
         * 搜索条件
         */
        private SqlQueryProviderSymbolEnum symbol;
        /**
         * 搜索值
         */
        private Object value;
    }
}
