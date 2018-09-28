package com.plugin.javawidget.enums;

import com.plugin.javawidget.driven.converters.JsonResultMsg;
import com.plugin.javawidget.driven.converters.JsonResultValue;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author LK
 * @version 1.0
 * @date 2017-12-27 20:18
 */
public enum SystemMsgEnums implements JsonResultMsg<Enum> {

	/**
	 * 成功
	 */
	@JsonResultValue(Code = @Value("${SystemMsgEnums.OPS_SUCCESS.code}"),Msg = @Value("${SystemMsgEnums.OPS_SUCCESS.msg}") )
	OPS_SUCCESS(true),

	/**
	 * 失败
	 */
	@JsonResultValue(Code = @Value("${SystemMsgEnums.OPS_FAILURE.code}"),Msg = @Value("${SystemMsgEnums.OPS_FAILURE.msg}") )
	OPS_FAILURE(false),

	/**
	 * 链接远程服务器失败
	 */
	@JsonResultValue(Code = @Value("${SystemMsgEnums.OPS_HTTP_FAILURE.code}"),Msg = @Value("${SystemMsgEnums.OPS_HTTP_FAILURE.msg}") )
	OPS_HTTP_FAILURE(false),

	/**
	 * 请求链接远程服务器失败
	 */
	@JsonResultValue(Code = @Value("${SystemMsgEnums.OPS_HTTP_RESPONSE_FAILURE.code}"),Msg = @Value("${SystemMsgEnums.OPS_HTTP_RESPONSE_FAILURE.msg}") )
	OPS_HTTP_RESPONSE_FAILURE(false),

	/**
	 * 请求远程服务器，远程服务器返回数据异常
	 */
	@JsonResultValue(Code = @Value("${SystemMsgEnums.OPS_HTTP_RESPONSE_FAILURE_DATA.code}"),Msg = @Value("${SystemMsgEnums.OPS_HTTP_RESPONSE_FAILURE_DATA.msg}") )
	OPS_HTTP_RESPONSE_FAILURE_DATA(false),

	/**
	 * Redis操作锁定中
	 */
	@JsonResultValue(Code = @Value("${SystemMsgEnums.OPS_REDIS_LOCK_DATA.code}"),Msg = @Value("${SystemMsgEnums.OPS_REDIS_LOCK_DATA.msg}") )
	OPS_REDIS_LOCK_DATA(false),

	/**
	 * 404 失败
	 */
	@JsonResultValue(Code = @Value("${SystemMsgEnums.OPS_404.code}"),Msg = @Value("${SystemMsgEnums.OPS_404.msg}") )
	OPS_404(false),


	/**
	 * 500 失败
	 */
	@JsonResultValue(Code = @Value("${SystemMsgEnums.OPS_500.code}"),Msg = @Value("${SystemMsgEnums.OPS_500.msg}") )
	OPS_500(false),

	/**
	 * 无登录用户信息 失败
	 */
	@JsonResultValue(Code = @Value("${SystemMsgEnums.OPS_USER_NOTTOKEN.code}"),Msg = @Value("${SystemMsgEnums.OPS_USER_NOTTOKEN.msg}") )
	OPS_USER_NOTTOKEN(false),

	/**
	 * 重复添加
	 */
	@JsonResultValue(Code = @Value("${SystemMsgEnums.OPS_Repeatedly_Added.code}"),Msg = @Value("${SystemMsgEnums.OPS_Repeatedly_Added.msg}") )
	OPS_Repeatedly_Added(false),

	/**
	 * 数据被其它地方引用
	 */
	@JsonResultValue(Code = @Value("${SystemMsgEnums.OPS_Occupied.code}"),Msg = @Value("${SystemMsgEnums.OPS_Occupied.msg}") )
	OPS_Occupied(false);

	private boolean success;

	SystemMsgEnums(boolean success) {
		this.success = success;
	}

	/**
	 * 获取Success结果
	 *
	 * @return
	 */
	@Override
	public boolean getSuccess() {
		return this.success;
	}
}
