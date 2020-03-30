package com.linkkou.spring.driven.resolvers;


import com.google.gson.Gson;
import com.linkkou.gson.processor.GsonAutowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.ModelFactory;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.lang.annotation.Annotation;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 自定义参数解析
 * JOSN格式的Requestbody转换为Model
 *
 * @author LK
 * @version 1.0
 * @data 2017-12-08 21:45
 */
public class JsonResolverImpl implements HandlerMethodArgumentResolver {

    private static Logger logger = LoggerFactory.getLogger("JsonResolverLogger");

    @GsonAutowired
    private static Gson gson;

    private List<String> httpMethod = Collections.singletonList("POST");

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        //返回设置注解
        return methodParameter.hasParameterAnnotation(JsonResolver.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        //转换的类型
        HttpServletRequest servletRequest = (HttpServletRequest) nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        final String methodname = servletRequest.getMethod();
        final String requesturi = servletRequest.getRequestURI();
        boolean inHttpMethod = false;
        for (String httpMethod : httpMethod) {
            if (httpMethod.equals(methodname)) {
                inHttpMethod = true;
            }
        }
        if (!inHttpMethod) {
            final String s = requesturi + methodname;
            logger.error(s);
            throw new IllegalArgumentException(s);
        }
        ServletServerHttpRequest inputMessage = new ServletServerHttpRequest(servletRequest);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        EmptyBodyCheckingHttpInputMessage emptyBodyCheckingHttpInputMessage = new EmptyBodyCheckingHttpInputMessage(inputMessage);
        InputStream d = emptyBodyCheckingHttpInputMessage.getBody();
        int i = -1;
        while ((i = d.read()) != -1) {
            baos.write(i);
        }
        String data = new String(baos.toByteArray(), StandardCharsets.UTF_8);
        servletRequest.setAttribute("XXXX", data);
        final Object o = gson.fromJson(data, methodParameter.getGenericParameterType());
        return validated(methodParameter, modelAndViewContainer, nativeWebRequest, webDataBinderFactory, o);
    }

    /**
     * 解决 servlet的request的inputstream只能被读一次
     */
    private class EmptyBodyCheckingHttpInputMessage {
        private final HttpHeaders headers;
        private final InputStream body;
        private final org.springframework.http.HttpMethod method;

        public EmptyBodyCheckingHttpInputMessage(HttpInputMessage inputMessage) throws IOException {
            this.headers = inputMessage.getHeaders();
            InputStream inputStream = inputMessage.getBody();
            if (inputStream == null) {
                this.body = null;
            } else if (inputStream.markSupported()) {
                inputStream.mark(1);
                this.body = inputStream.read() != -1 ? inputStream : null;
                inputStream.reset();
            } else {
                PushbackInputStream pushbackInputStream = new PushbackInputStream(inputStream);
                int b = pushbackInputStream.read();
                if (b == -1) {
                    this.body = null;
                } else {
                    this.body = pushbackInputStream;
                    pushbackInputStream.unread(b);
                }
            }
            this.method = ((HttpRequest) inputMessage).getMethod();
        }

        public HttpHeaders getHeaders() {
            return this.headers;
        }

        public InputStream getBody() throws IOException {
            return this.body;
        }

        public HttpMethod getMethod() {
            return this.method;
        }
    }

    private Object validated(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory, Object attribute) throws Exception {
        String name = ModelFactory.getNameForParameter(methodParameter);
        WebDataBinder binder = webDataBinderFactory.createBinder(nativeWebRequest, attribute, name);
        Object target = binder.getTarget();
        if (target != null) {
            validateIfApplicable(binder, methodParameter);
            if (binder.getBindingResult().hasErrors() && isBindExceptionRequired(binder, methodParameter)) {
                throw new BindException(binder.getBindingResult());
            }
        }
        // Add resolved attribute and BindingResult at the end of the model
        Map<String, Object> bindingResultModel = binder.getBindingResult().getModel();
        modelAndViewContainer.removeAttributes(bindingResultModel);
        modelAndViewContainer.addAllAttributes(bindingResultModel);
        return binder.convertIfNecessary(binder.getTarget(), methodParameter.getParameterType(), methodParameter);
    }

    private void validateIfApplicable(WebDataBinder binder, MethodParameter methodParam) {
        Annotation[] annotations = methodParam.getParameterAnnotations();
        for (Annotation ann : annotations) {
            Validated validatedAnn = AnnotationUtils.getAnnotation(ann, Validated.class);
            if (validatedAnn != null || ann.annotationType().getSimpleName().startsWith("Valid")) {
                Object hints = (validatedAnn != null ? validatedAnn.value() : AnnotationUtils.getValue(ann));
                Object[] validationHints = (hints instanceof Object[] ? (Object[]) hints : new Object[]{hints});
                binder.validate(validationHints);
                break;
            }
        }
    }

    private boolean isBindExceptionRequired(WebDataBinder binder, MethodParameter methodParam) {
        int i = methodParam.getParameterIndex();
        Class<?>[] paramTypes = methodParam.getMethod().getParameterTypes();
        boolean hasBindingResult = (paramTypes.length > (i + 1) && Errors.class.isAssignableFrom(paramTypes[i + 1]));
        return !hasBindingResult;
    }

    public JsonResolverImpl setHttpMethod(List<String> HttpMethod) {
        this.httpMethod = HttpMethod;
        return this;
    }

    public List<String> getHttpMethod() {
        return this.httpMethod;
    }


}
