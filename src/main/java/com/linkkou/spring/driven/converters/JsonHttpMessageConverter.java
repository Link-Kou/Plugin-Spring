package com.linkkou.spring.driven.converters;

import com.google.gson.Gson;
import com.linkkou.gson.processor.GsonAutowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 全局输出转换
 *
 * @author LK
 * @version 1.0
 * @data 2017-12-08 21:45
 */
public class JsonHttpMessageConverter extends AbstractGenericHttpMessageConverter<Object> {

    private static Logger logger = LoggerFactory.getLogger("JsonHttpMessageConverterLogger");

    @GsonAutowired
    private static Gson gson;

    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    /**
     * 设置头编码
     *
     * @param headers
     * @return
     */
    private Charset getCharset(HttpHeaders headers) {
        return headers != null && headers.getContentType() != null && headers.getContentType().getCharset() != null ? headers.getContentType().getCharset() : DEFAULT_CHARSET;
    }

    /**
     * 输出转换
     *
     * @param o                 数据
     * @param type              类型
     * @param httpOutputMessage http
     * @throws IOException
     * @throws HttpMessageNotWritableException
     */
    @Override
    protected void writeInternal(Object o, Type type, HttpOutputMessage httpOutputMessage) throws IOException, HttpMessageNotWritableException {
        Charset charset = this.getCharset(httpOutputMessage.getHeaders());
        try (OutputStreamWriter writer = new OutputStreamWriter(httpOutputMessage.getBody(), charset)) {
            if (type != null) {
                gson.toJson(o, type, writer);
            } else {
                gson.toJson(o, writer);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new HttpMessageNotWritableException("Could not write Json: " + e.getMessage(), e);
        }
    }


    @Override
    protected boolean supports(Class<?> aClass) {
        throw new UnsupportedOperationException();
    }


    /**
     * 输入读取
     *
     * @param aClass           类型
     * @param httpInputMessage http
     * @return Object
     * @throws IOException
     * @throws HttpMessageNotReadableException
     */
    @Override
    protected Object readInternal(Class<?> aClass, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException {
        return null;
    }

    /**
     * 输入转换
     *
     * @param type             类型
     * @param aClass           类型
     * @param httpInputMessage http
     * @return Object
     */
    @Override
    public Object read(Type type, Class<?> aClass, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException {
        return this.readInternal(aClass, httpInputMessage);
    }

    /**
     * 是否控制输入
     *
     * @param clazz     类型
     * @param mediaType HTTP头标识
     * @return boolean
     */
    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return false;
    }

    /**
     * 是否控制输出
     *
     * @param clazz     返回对象类型
     * @param mediaType HTTP头标识
     * @return boolean
     */
    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return clazz.equals(JsonResult.class);
    }

}