package com.plugin.javawidget.driven.interceptor;

import com.plugin.javawidget.driven.converters.JsonResult;
import com.plugin.javawidget.driven.converters.JsonResultMsg;
import com.plugin.json.Json;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 用于拦截器输出专用
 * @author LK
 * @date 2018/3/28
 * @description
 */
public class InterceptorResponse {

	private static class json extends Json {
		@Override
		protected String toJson(Object src) {
			return super.toJson(src);
		}
	}

	/**
	 * 输出错误信息
	 *
	 * @param msg
	 * @param response
	 */
	public static void responseMsg(HttpServletResponse response, JsonResultMsg<Enum> msg) {
		JsonResult jsonResult = new JsonResult(msg);
		if (!response.isCommitted()) {
			try {
				String json = new json().toJson(jsonResult.getMap());
				if (!response.isCommitted()){
					response.setCharacterEncoding("UTF-8");
				}
				//这句话是解决乱码的
				response.setHeader("Content-Type", "text/html;charset=UTF-8");
				response.getWriter().write(json);
				response.getWriter().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
