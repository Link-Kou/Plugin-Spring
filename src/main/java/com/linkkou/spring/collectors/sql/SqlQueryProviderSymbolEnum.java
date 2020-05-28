package com.linkkou.spring.collectors.sql;

import com.google.gson.JsonElement;
import com.linkkou.gson.typefactory.GsonEnum;

/**
 * @author lk
 * @version 1.0
 * @date 2020/5/21 14:33
 */
public enum SqlQueryProviderSymbolEnum implements GsonEnum<SqlQueryProviderSymbolEnum> {
    /**
     * 未知
     */
    UNKNOWN(""),

    /**
     * =
     */
    Equal("="),

    /**
     * <>
     */
    NotEqualTo("<>"),

    /**
     * <
     */
    Islessthan("<"),

    /**
     * >
     */
    Greaterthan(">"),

    /**
     * <=
     */
    IslessthanEqualTo("<="),

    /**
     * >=
     */
    GreaterthanEqualTo(">="),

    /**
     * in
     */
    IN("in"),

    /**
     * not in
     */
    NotIN("not in");

    /**
     * 类型
     */
    private final String type;

    SqlQueryProviderSymbolEnum(String type) {
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
    public SqlQueryProviderSymbolEnum deserialize(JsonElement jsonEnum) {
        for (SqlQueryProviderSymbolEnum data : SqlQueryProviderSymbolEnum.values()) {
            if (data.type.equals(jsonEnum.getAsString())) {
                return data;
            }
        }
        return SqlQueryProviderSymbolEnum.UNKNOWN;
    }

    /**
     * Mybatis 获取到值
     *
     * @param o 值
     * @return 枚举
     */
    @Override
    public <T> SqlQueryProviderSymbolEnum convert(T o) {
        return null;
    }

    public String getType() {
        return this.type;
    }
}
