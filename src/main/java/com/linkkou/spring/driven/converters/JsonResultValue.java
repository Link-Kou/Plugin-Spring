package com.linkkou.spring.driven.converters;


import org.springframework.beans.factory.annotation.Value;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
/**
 * 用于枚举上的注解，实现与Properties文件交互
 */
public @interface JsonResultValue {
	/**
	 * code
	 * @return
	 */
	Value Code();

	/**
	 * 消息解释
	 * @return
	 */
	Value Msg();
}
