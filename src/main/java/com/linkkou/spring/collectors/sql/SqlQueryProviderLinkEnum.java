package com.linkkou.spring.collectors.sql;

import com.google.gson.JsonElement;
import com.linkkou.gson.typefactory.GsonEnum;

/**
 * AND OR 的连接符
 *
 * @author lk
 * @version 1.0
 * @date 2020/5/21 14:33
 */
public enum SqlQueryProviderLinkEnum implements GsonEnum<SqlQueryProviderLinkEnum> {
    /**
     * 未知
     */
    UNKNOWN(""),

    /**
     * expression
     */
    AND("AND"),

    /**
     * group
     */
    OR("OR");

    /**
     * 类型
     */
    private final String type;

    SqlQueryProviderLinkEnum(String type) {
        this.type = type;
    }

    /**
     * 序列化
     *
     * @return
     */
    @Override
    public Object serialize() {
        return type;
    }

    /**
     * 反序列化
     *
     * @param jsonEnum
     * @return
     */
    @Override
    public SqlQueryProviderLinkEnum deserialize(JsonElement jsonEnum) {
        for (SqlQueryProviderLinkEnum data : SqlQueryProviderLinkEnum.values()) {
            if (data.type.equals(jsonEnum.getAsString())) {
                return data;
            }
        }
        return SqlQueryProviderLinkEnum.UNKNOWN;
    }

    /**
     * Mybatis 获取到值
     *
     * @param o 值
     * @return 枚举
     */
    @Override
    public <T> SqlQueryProviderLinkEnum convert(T o) {
        return null;
    }

    public String getType() {
        return this.type;
    }
}
