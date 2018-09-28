package com.plugin.javawidget.basemodel;

import java.lang.annotation.*;

/**
 * Created by LK on 2017/6/28.
 * JOSN格式的Requestbody转换为Model
 * 发生错误返回null
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JsonBaseParam {
}
