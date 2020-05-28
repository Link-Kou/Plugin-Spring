package com.linkkou.spring.collectors.sql;

import com.google.gson.JsonElement;
import com.linkkou.gson.typefactory.GsonEnum;

/**
 * @author lk
 * @version 1.0
 * @date 2020/5/21 14:33
 */
public enum SqlQueryProviderTypeEnum implements GsonEnum<SqlQueryProviderTypeEnum> {
    /**
     * 未知
     */
    UNKNOWN(""),

    /**
     * expression
     */
    Expression("expression"),

    /**
     * group
     */
    Group("group");

    /**
     * 类型
     */
    private final String type;

    SqlQueryProviderTypeEnum(String type) {
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
    public SqlQueryProviderTypeEnum deserialize(JsonElement jsonEnum) {
        for (SqlQueryProviderTypeEnum data : SqlQueryProviderTypeEnum.values()) {
            if (data.type.equals(jsonEnum.getAsString())) {
                return data;
            }
        }
        return SqlQueryProviderTypeEnum.UNKNOWN;
    }

    /**
     * Mybatis 获取到值
     *
     * @param o 值
     * @return 枚举
     */
    @Override
    public <T> SqlQueryProviderTypeEnum convert(T o) {
        return null;
    }
}
