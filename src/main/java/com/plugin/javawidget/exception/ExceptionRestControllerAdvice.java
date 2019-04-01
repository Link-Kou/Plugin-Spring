package com.plugin.javawidget.exception;

import com.plugin.javawidget.driven.converters.JsonResult;
import com.plugin.javawidget.enums.SystemMsgEnums;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * 全局统一错误捕捉
 * @author lk
 * @version 1.0
 * @data 2017-06-21 13:05
 */
@RestControllerAdvice
@RestController
public class ExceptionRestControllerAdvice {

	private static final Logger logger = LoggerFactory.getLogger(ExceptionRestControllerAdvice.class);

	/**
	 * TODO 返回输出走的是系统默认：
	 * {@link org.springframework.http.converter.json.GsonHttpMessageConverter}
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = Throwable.class)
	public JsonResult error(Throwable e) {
		logger.error(" Throwable: " + e.getMessage(), e);
		return new JsonResult(SystemMsgEnums.OPS_FAILURE);
	}

	/**
	 * 未知错误
	 */
	@ExceptionHandler(value = Exception.class)
	public JsonResult scheduleError(Exception e) throws Exception {
		logger.error(" Exception: " + e.getMessage(), e);
		return new JsonResult(SystemMsgEnums.OPS_FAILURE);
	}


	@RequestMapping(value = "/404")
	public JsonResult error404() {
		return new JsonResult(SystemMsgEnums.OPS_404);
	}

	@RequestMapping(value = "/500")
	public JsonResult error500() {
		return new JsonResult(SystemMsgEnums.OPS_500);
	}

}
