package com.plugin.javawidget.cookie;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Cookie 工具
 */
public class CookiesUtil {

	/**
	 * @title:根据名字获取cookie
	 * @description:
	 * @param request
	 * @param name
	 * @return
	 * @date:2017年11月13日
	 */
	public static String getCookieByName(HttpServletRequest request, String name) {
		Map<String, Cookie> cookieMap = readCookieMap(request);
		if (cookieMap.containsKey(name)) {
			Cookie cookie = (Cookie) cookieMap.get(name);
			return cookie.getValue();
		} else {
			return null;
		}

	}

	/**
	 * @title:将cookie封装到Map里面
	 * @description:
	 * @param request
	 * @return
	 * @date:2017年11月13日
	 */
	private static Map<String, Cookie> readCookieMap(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		Map<String, Cookie> cookieMap = new HashMap<String, Cookie>(16);
		if (null != cookies) {
			for (Cookie cookie : cookies) {
				cookieMap.put(cookie.getName(), cookie);
			}
		}
		return cookieMap;
	}

	/**
	 * @title:保存Cookies
	 * @description:
	 * @param response
	 * @param name
	 * @param value
	 * @param time
	 * @return
	 * @date:2017年11月13日
	 */
	public static HttpServletResponse setCookie(HttpServletResponse response, String name, String value, int time) {
		// new一个Cookie对象,键值对为参数
		Cookie cookie = new Cookie(name, value);
		// tomcat下多应用共享
		cookie.setPath("/");
		// 如果cookie的值中含有中文时，需要对cookie进行编码，不然会产生乱码
		cookie.setHttpOnly(true);
		try {
			URLEncoder.encode(value, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		cookie.setMaxAge(time);
		// 将Cookie添加到Response中,使之生效
		// addCookie后，如果已经存在相同名字的cookie，则最新的覆盖旧的cookie
		response.addCookie(cookie);
		return response;
	}
}