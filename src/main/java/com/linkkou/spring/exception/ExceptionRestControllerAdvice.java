package com.linkkou.spring.exception;

import com.linkkou.spring.driven.converters.JsonResult;
import com.linkkou.spring.driven.converters.JsonResultException;
import com.linkkou.spring.enums.SystemMsgEnums;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;


/**
 * 全局统一错误捕捉
 *
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
     *
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

    /**
     * Validated 校验错误
     */
    @ExceptionHandler(value = BindException.class)
    public JsonResult scheduleError(BindException e) throws Exception {
        logger.error(" BindException: " + e.getMessage(), e);
        return new JsonResult(SystemMsgEnums.OPS_VALIDATION_EXCEPTION);
    }

    /**
     * Validated 校验错误
     */
    @ExceptionHandler(value = javax.validation.ValidationException.class)
    public JsonResult scheduleError(javax.validation.ValidationException e) throws Exception {
        logger.error(" ValidationException: " + e.getMessage(), e);
        return new JsonResult(SystemMsgEnums.OPS_VALIDATION_EXCEPTION);
    }

    /**
     * JsonResult 统一输出
     */
    @ExceptionHandler(value = JsonResultException.class)
    public JsonResult scheduleError(JsonResultException e) throws Exception {
        logger.error(" JsonResultException: " + e.getMessage(), e);
        return new JsonResult(e.getMsgtype());
    }

    /**
     * 响应400/500错误
     */
    @ExceptionHandler(value = TypeMismatchException.class)
    public JsonResult scheduleError(TypeMismatchException e) throws Exception {
        logger.error(" JsonResultException: " + e.getMessage(), e);
        return new JsonResult(SystemMsgEnums.OPS_500, e.getMessage());
    }

    /**
     * 响应404 错误
     */
    @ExceptionHandler(value = NoHandlerFoundException.class)
    public JsonResult scheduleError(NoHandlerFoundException e) throws Exception {
        logger.error(" JsonResultException: " + e.getMessage(), e);
        return new JsonResult(SystemMsgEnums.OPS_404, e.getRequestURL());
    }


    @RequestMapping(value = "/404")
    public JsonResult error404(HttpServletRequest request, HttpServletResponse response) {
        final HttpServletRequestWrapper request1 = (HttpServletRequestWrapper) request;
        final ServletRequest request2 = request1.getRequest();
        final String requestURI = ((HttpServletRequest) request2).getRequestURI();
        return new JsonResult(requestURI, SystemMsgEnums.OPS_404);
    }

    @RequestMapping(value = "/500")
    public JsonResult error500(HttpServletRequest request, HttpServletResponse response) {
        return new JsonResult(SystemMsgEnums.OPS_500);
    }

}
