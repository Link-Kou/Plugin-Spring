package com.plugin.javawidget.driven.converters;

/**
 * 输出必须为枚举，并且实现JsonResultMsg接口
 * Created by LK on 2017/4/26.
 * @author LK
 * @version 1.0
 * @data 2017-12-08 21:45
 */
public interface JsonResultMsg<T extends Enum> {

	/**
	 * 获取Success结果
	 * @return
	 */
	boolean getSuccess();

}
