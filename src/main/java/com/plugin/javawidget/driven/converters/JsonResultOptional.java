package com.plugin.javawidget.driven.converters;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author lk
 * @date 2018/10/2 21:47
 */
public class JsonResultOptional<T> {

    private T data;

    private JsonResult jsonResult;

    public JsonResultOptional(T data) {
        this.data = data;
    }


    public JsonResultOptional<T> orElse(JsonResultFunctionInitialize.JsonResultFunction1<T> b, JsonResultMsg<Enum> value) {
        if (b.get(this.data)) {
            this.jsonResult = new JsonResult(this.data,value);
        }
        return this;
    }


    public JsonResultOptional<T> orElseReturn(JsonResultFunctionInitialize.JsonResultFunction2<T> b) {
        this.jsonResult = b.get(this.data);
        return this;
    }

    public JsonResult get() {
        return jsonResult;
    }
}
