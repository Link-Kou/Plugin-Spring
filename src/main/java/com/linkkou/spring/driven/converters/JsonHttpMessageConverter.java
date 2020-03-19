package com.linkkou.spring.driven.converters;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.linkkou.gson.processor.GsonAutowired;
import com.plugin.configproperty.Config;
import com.plugin.configproperty.ConfigValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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

/**
 * 全局输出转换
 * @author LK
 * @version 1.0
 * @data 2017-12-08 21:45
 */
public class JsonHttpMessageConverter extends AbstractGenericHttpMessageConverter<Object> {

	private static Logger logger = LoggerFactory.getLogger("JsonHttpMessageConverterLogger");

	@GsonAutowired
	private static Gson gson;

	@ConfigValue(@Value("${SystemMsgEnums.ERROR.REDISLOCKAOPIMPL.JSONRESOLVERIMPL.msg}"))
	private transient Config<String> JSONRESOLVERIMPL;


	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

	/**
	 * 设置头编码
	 * @param headers
	 * @return
	 */
	private Charset getCharset(HttpHeaders headers) {
		return headers != null && headers.getContentType() != null && headers.getContentType().getCharset() != null ? headers.getContentType().getCharset() : DEFAULT_CHARSET;
	}

	/**
	 * 输出转换
	 * @param o
	 * @param type
	 * @param httpOutputMessage
	 * @throws IOException
	 * @throws HttpMessageNotWritableException
	 */
	@Override
	protected void writeInternal(Object o, Type type, HttpOutputMessage httpOutputMessage) throws IOException, HttpMessageNotWritableException {
		Charset charset = this.getCharset(httpOutputMessage.getHeaders());
		OutputStreamWriter writer = new OutputStreamWriter(httpOutputMessage.getBody(), charset);
		try {
			if (type != null) {
				gson.toJson(o, type, writer);
			} else {
				gson.toJson(o, writer);
			}
		} catch (Exception e) {
			logger.error(JSONRESOLVERIMPL+e.getMessage());
			e.printStackTrace();
			throw new HttpMessageNotWritableException("Could not write Json: " + e.getMessage(), e);
		}finally {
			writer.close();
		}
	}


	@Override
	protected boolean supports(Class<?> aClass) {
		throw new UnsupportedOperationException();
	}


	/**
	 * 输入读取
	 * @param aClass
	 * @param httpInputMessage
	 * @return
	 * @throws IOException
	 * @throws HttpMessageNotReadableException
	 */
	@Override
	protected Object readInternal(Class<?> aClass, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException {
		return null;
	}

	/**
	 * 输入转换
	 * @param type
	 * @param aClass
	 * @param httpInputMessage
	 * @return
	 * @throws IOException
	 * @throws HttpMessageNotReadableException
	 */
	@Override
	public Object read(Type type, Class<?> aClass, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException {
		return this.readInternal(aClass,httpInputMessage);
	}

	/**
	 * 是否控制输入
	 * @param clazz
	 * @param mediaType
	 * @return
	 */
	@Override
	public boolean canRead(Class<?> clazz, MediaType mediaType) {
		return false;
	}

	/**
	 * 是否控制输出
	 * @param clazz 返回对象类型
	 * @param mediaType HTTP头标识
	 * @return
	 */
	@Override
	public boolean canWrite(Class<?> clazz, MediaType mediaType) {
		return clazz.equals(JsonResult.class);
	}

}