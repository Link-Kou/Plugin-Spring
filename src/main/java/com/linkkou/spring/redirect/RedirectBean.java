package com.linkkou.spring.redirect;

import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 重定向
 * @author lk
 * @version 1.0
 * @date 2020/11/1 10:58
 */
public class RedirectBean {

    private HttpServletRequest request;

    private HttpServletResponse response;

    @Autowired
    public RedirectBean(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    private void sendRedirect(String url) {
        try {
            response.sendRedirect(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
