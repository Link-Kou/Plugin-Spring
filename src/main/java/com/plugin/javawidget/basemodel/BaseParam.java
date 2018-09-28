package com.plugin.javawidget.basemodel;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 基础输入参数
 * @author LK
 * @version 1.0
 * @data 2017-12-22 9:17
 */
@Getter
@Setter
@Accessors(chain = true)
public class BaseParam {

	/**
	 * 版本号
	 */
	@NotEmpty
	private String version;
	/**
	 * 对接者
	 */
	@NotEmpty
	private String devTokenId;
	/**
	 * 业务
	 */
	@NotNull
	private Integer business;
	/**
	 * 平台
	 */
	@NotNull
	private Integer platform;
	/**
	 * 系统
	 */
	@NotNull
	private Integer system;
	/**
	 * 自定义
	 */
	private String custom;

}