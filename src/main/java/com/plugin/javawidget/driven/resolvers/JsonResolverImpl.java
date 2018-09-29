package com.plugin.javawidget.driven.resolvers;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.plugin.configproperty.Config;
import com.plugin.configproperty.ConfigValue;
import com.plugin.json.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

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


    @ConfigValue(@Value("${SystemMsgEnums.ERROR.JSONRESOLVERIMPL.HTTPMETHOD.msg}"))
    private transient Config<String> HTTPMETHOD;

    @ConfigValue(@Value("${SystemMsgEnums.ERROR.JSONRESOLVERIMPL.BYTEARRAYOUTPUTSTREAM.msg}"))
    private transient Config<String> BYTEARRAYOUTPUTSTREAM;

    @ConfigValue(@Value("${SystemMsgEnums.HTTPSERVLETREQUEST.msg}"))
    private transient Config<String> HTTPSERVLETREQUEST;

    @ConfigValue(@Value("${SystemMsgEnums.ERROR.REDISLOCKAOPIMPL.ISNOTJSONTYPE.msg}"))
    private transient Config<String> ISNOTJSONTYPE;



    private class json extends Json {
        @Override
        protected <T> T fromJson(String json, Type type) {
            return super.fromJson(json, type);
        }

        @Override
        protected <T> T fromJson(JsonElement json, Type type) {
            return super.fromJson(json, type);
        }
    }

    private List<String> HttpMethod = Arrays.asList("POST");

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        //返回设置注解
        return methodParameter.hasParameterAnnotation(JsonResolver.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) {
        //转换的类型

        HttpServletRequest servletRequest = (HttpServletRequest)nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        final String methodname =  servletRequest.getMethod();
        final String requesturi =  servletRequest.getRequestURI();

        boolean inHttpMethod = false;
        for (String httpMethod : HttpMethod) {
            if (httpMethod.equals(methodname)) {
                inHttpMethod = true;
            }
        }
        if (!inHttpMethod) {
            final String s = requesturi + HTTPMETHOD.get() + methodname;
            logger.error(s);
            throw new IllegalArgumentException(s);
        }


        ServletServerHttpRequest inputMessage = new ServletServerHttpRequest(servletRequest);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        EmptyBodyCheckingHttpInputMessage emptyBodyCheckingHttpInputMessage;
        try {
            emptyBodyCheckingHttpInputMessage = new EmptyBodyCheckingHttpInputMessage(inputMessage);
            InputStream d = emptyBodyCheckingHttpInputMessage.getBody();
            int i = -1;
            while ((i = d.read()) != -1) {
                baos.write(i);
            }
        } catch (Exception e) {
            logger.error(BYTEARRAYOUTPUTSTREAM.get());
            throw new IllegalArgumentException(BYTEARRAYOUTPUTSTREAM.get());
        }
        String data = new String(baos.toByteArray(), StandardCharsets.UTF_8);
        servletRequest.setAttribute(HTTPSERVLETREQUEST.get(),data);
        try {
            return new json().fromJson(data, methodParameter.getGenericParameterType());
        }catch (Exception e){
            logger.error(requesturi + data + ISNOTJSONTYPE.get());
            throw e ;
        }
    }


    /**
     * servlet的request的inputstream只能被读一次
     */
    private class EmptyBodyCheckingHttpInputMessage{
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
            this.method = ((HttpRequest)inputMessage).getMethod();
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


    /**
     * 分页解析支持
     * {
     * page:
     * itemsPerPage:
     * data:
     * ......:
     * }
     *
     * @param data
     * @param typeT
     * @return
     */
    @Deprecated
    private <T> T compatiblePaginator(String data, Type[] typeT) {
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(data).getAsJsonObject();
        if (object.has("data")) {
            return new json().fromJson(object.get("data"), typeT[0]);
        } else {
            return new json().fromJson(data, typeT[0]);
        }
    }


    public JsonResolverImpl setHttpMethod(List<String> HttpMethod) {
        this.HttpMethod = HttpMethod;
        return this;
    }

    public List<String> getHttpMethod() {
        return this.HttpMethod;
    }


}
