package com.linkkou.spring.basemodel;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.linkkou.gson.processor.GsonAutowired;
import com.plugin.configproperty.Config;
import com.plugin.configproperty.ConfigValue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * 自定义参数解析
 * JOSN格式的Requestbody转换为Model
 *
 * @author LK
 * @version 1.0
 * @data 2017-12-08 21:45
 */
public class JsonBaseParamImpl implements HandlerMethodArgumentResolver {

    @GsonAutowired
    private static Gson gson;

    @ConfigValue(@Value("${Globalparam.SYS_AUTH_DEV_NAME}"))
    private Config<String> SYS_AUTH_DEV_NAME;

    @ConfigValue(@Value("${Globalparam.SYS_AUTH_DEV_INTERCEPTOR}"))
    private Config<Boolean> SYS_AUTH_DEV_INTERCEPTOR;


    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        //返回设置注解
        return methodParameter.hasParameterAnnotation(JsonBaseParam.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) {
        HttpServletRequest httpServletRequest = ((ServletWebRequest) nativeWebRequest).getRequest();
        BaseParam baseParam2 = new BaseParam();
        //#region 开发者信息加入参数内
        if (SYS_AUTH_DEV_INTERCEPTOR.get()) {
            //配合拦截器实现开发对接者信息解析
            final Object attribute = httpServletRequest.getAttribute(SYS_AUTH_DEV_NAME.get());
            if (attribute instanceof BaseParam) {
                BaseParam baseParam = (BaseParam) attribute;
                baseParam2.setSystem(baseParam.getSystem())
                        .setVersion(baseParam.getVersion())
                        .setBusiness(baseParam.getBusiness())
                        .setCustom(baseParam.getCustom())
                        .setPlatform(baseParam.getPlatform())
                        .setDevTokenId(baseParam.getDevTokenId());
            } else {
                throw new IllegalArgumentException("无法识别开发者信息");
            }
        }
        return baseParam2;
        //#endregion
    }

}
