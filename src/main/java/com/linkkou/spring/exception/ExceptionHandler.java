package com.linkkou.spring.exception;

import com.linkkou.spring.driven.converters.JsonResult;
import com.linkkou.spring.enums.SystemMsgEnums;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * 错误捕捉
 * @author lk
 * @version 1.0
 * @date 2020/3/21 16:27
 */
@Component
public class ExceptionHandler implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        ErrorJsonView ejv = new ErrorJsonView();
        return new ModelAndView(ejv, "JsonResult", new JsonResult(SystemMsgEnums.OPS_404, e.getMessage()));
    }

    public class ErrorJsonView extends AbstractView {

        private final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

        @Override
        protected void renderMergedOutputModel(Map<String, Object> map, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
            httpServletResponse.setContentType("text/json; charset=UTF-8");
            map.get("JsonResult");
            try (PrintWriter out = httpServletResponse.getWriter()) {
                out.write("asdasd");
                out.flush();
            } catch (IOException e) {
                logger.error("com.gttown.common.support.web.view.ErrorJsonView", e);
            }
        }
    }
}