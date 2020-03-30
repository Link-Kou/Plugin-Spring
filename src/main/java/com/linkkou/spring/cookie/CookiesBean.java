package com.linkkou.spring.cookie;

import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Cookie 工具
 * @author lk
 * @version 1.0
 * @date 2020/3/21 16:27
 */
public class CookiesBean {


    private HttpServletRequest request;

    private HttpServletResponse response;

    @Autowired
    public CookiesBean(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    /**
     * 根据名字获取cookie
     *
     * @param name
     */
    public String getCookieByName(String name) {
        Map<String, Cookie> cookieMap = readCookieMap();
        if (cookieMap.containsKey(name)) {
            Cookie cookie = (Cookie) cookieMap.get(name);
            return cookie.getValue();
        } else {
            return null;
        }
    }

    /**
     * 根据多个名字获取cookie值
     *
     * @param name
     */
    public Map<String, String> getCookieByName(String... name) {
        Map<String, String> cookieValue = new HashMap<>(16);
        Map<String, Cookie> cookieMap = readCookieMap();
        for (String s : name) {
            if (cookieMap.containsKey(s)) {
                Cookie cookie = (Cookie) cookieMap.get(s);
                cookieValue.put(s, cookie.getValue());
            }
        }
        return cookieValue;
    }

    /**
     * 将cookie封装到Map里面
     *
     * @date:2017年11月13日
     */
    private Map<String, Cookie> readCookieMap() {
        Cookie[] cookies = this.request.getCookies();
        Map<String, Cookie> cookieMap = new HashMap<String, Cookie>(16);
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie);
            }
        }
        return cookieMap;
    }

    /**
     * 保存Cookies
     * 默认过期时间为12小时
     *
     * @param name  名称
     * @param value 值
     * @date
     */
    public void setCookie(String name, String value) {
        setCookie(name, value, 12 * 60 * 60, true);
    }

    /**
     * 保存Cookies
     * 默认过期时间为12小时
     *
     * @param name  名称
     * @param value 值
     * @param httpOnly 只读
     * @date
     */
    public void setCookie(String name, String value, boolean httpOnly) {
        setCookie(name, value, 12 * 60 * 60, httpOnly);
    }


    /**
     * 保存Cookies
     *
     * @param name  名称
     * @param value 值
     * @param time  过期时间单位为秒
     * @date
     */
    public void setCookie(String name, String value, int time, boolean httpOnly) {
        // new一个Cookie对象,键值对为参数
        Cookie cookie = new Cookie(name, value);
        // tomcat下多应用共享
        cookie.setPath("/");
        // 是否后端只读
        cookie.setHttpOnly(httpOnly);
        try {
            URLEncoder.encode(value, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        cookie.setMaxAge(time);
        // 将Cookie添加到Response中,使之生效
        // addCookie后，如果已经存在相同名字的cookie，则最新的覆盖旧的cookie
        this.response.addCookie(cookie);
    }
}