package com.plugin.javawidget.driven.converters;

/**
 * @author: LK
 * @date: 2018/3/31
 * @description:
 */
public interface JsonResultFunctionInitialize<T> {


	interface JsonResultFunction1<T>{
		boolean get(T t);
	}

	interface JsonResultFunction2<T>{
		JsonResult get(T t);
	}


}
