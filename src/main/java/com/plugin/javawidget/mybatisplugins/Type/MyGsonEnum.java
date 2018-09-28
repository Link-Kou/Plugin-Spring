package com.plugin.javawidget.mybatisplugins.Type;

import com.google.gson.JsonElement;
import com.plugin.json.serializer.GsonEnum;

/**
 * @author lk
 * @date 2018/9/26 09:07
 */
public class MyGsonEnum implements GsonEnum {

    private Integer value;

    public MyGsonEnum(Integer value) {
        this.value = value;
    }

    @Override
    public Object serialize() {
        return null;
    }

    @Override
    public Object deserialize(JsonElement jsonElement) {
        return null;
    }

    @Override
    public Object get() {
        return null;
    }


    public Integer getValue() {
        return this.value;
    }

    public MyGsonEnum setValue(Integer value) {
        this.value = value;
        return this;
    }
}
