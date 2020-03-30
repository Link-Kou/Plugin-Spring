package com.linkkou.spring.driven.resolvers;

import java.lang.annotation.*;

/**
 * JOSN格式的Requestbody转换为Model
 * 发生错误返回null
 *
 * @author LK
 * @date 2017/6/28
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JsonResolver {
}
