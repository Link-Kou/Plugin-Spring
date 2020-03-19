package com.linkkou.spring.driven.interceptor;

import com.google.gson.Gson;
import com.linkkou.gson.processor.GsonAutowired;
import com.linkkou.spring.driven.converters.JsonResult;
import com.linkkou.spring.driven.converters.JsonResultMsg;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用于拦截器输出专用
 * @author LK
 * @date 2018/3/28
 * @description
 */
public class InterceptorResponse {

	@GsonAutowired
	private static Gson gson;

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
				String json = gson.toJson(jsonResult.getMap());
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
