package com.linkkou.spring.driven.converters;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.linkkou.configproperty.Config;
import com.linkkou.configproperty.ConfigUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * 统一输出返回 输出模版
 *
 * @author LK
 * @version 1.0
 * @date 2017-12-08 19:29
 */
public class JsonResult {

    private static Logger logger = LoggerFactory.getLogger("JsonResultLogger");

    private static Pattern p = Pattern.compile("[\\$\\{\\}]");

    private static final String CODENAME = "code";

    private static final String MSGNAME = "msg";

    private static final String DATANAME = "data";

    private static final String SUCCESSNAME = "success";

    /**
     * 失败
     */
    private static final int ERRORCODE = -100000;

    /**
     * 编号
     */
    public Integer code;

    /**
     * 消息
     */
    private String msg;

    /**
     * 消息枚举类型
     */
    private transient JsonResultMsg msgtype;

    /**
     * 消息枚举原生类型
     */
    private transient Enum<?> enumtype;

    /**
     * 数据类型
     */
    private Object data;

    /**
     * 状态
     */
    private boolean success;

    /**
     * 编号
     */
    public Integer getCode() {
        return code;
    }

    /**
     * 消息
     *
     * @return String
     */
    public String getMsg() {
        return this.msg;
    }

    /**
     * 消息枚举
     *
     * @return Enum
     */
    public JsonResultMsg getMsgtype() {
        return this.msgtype;
    }

    /**
     * 获取 消息枚举原生类型
     */
    public Enum<?> getEnumtype() {
        return this.enumtype;
    }

    /**
     * 状态
     *
     * @return boolean
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * 数据
     *
     * @return E
     */
    public <T> T getData() {
        return (T) this.data;
    }


    /**
     * 异常输出
     *
     * @param exception
     */
    public JsonResult(Throwable exception) {
        this.success = false;
        this.code = ERRORCODE;
        this.msg = exception.getMessage();
    }


    /**
     * 枚举配置输出
     *
     * @param value 枚举
     */
    public JsonResult(JsonResultMsg value) {
        this.jsonResult(null, value);
    }


    /**
     * 枚举配置输出,bool判断输出指定枚举
     *
     * @param val    bool
     * @param value1 true 执行
     * @param value2 false 执行
     */
    public JsonResult(boolean val, JsonResultMsg value1, JsonResultMsg value2) {
        if (val) {
            this.jsonResult(null, value1);
        } else {
            this.jsonResult(null, value2);
        }
    }

    /**
     * 枚举配置输出,自定义Msg输出
     * <br/>
     * <b>警告：此方法用于与HTTP请求或其他请求输出远程服务</b>
     *
     * @param value 枚举
     * @param msg   自定义消息
     */
    @SuppressWarnings("unchecked")
    public JsonResult(JsonResultMsg value, String msg) {
        this.jsonResult(null, value, msg);
    }

    /**
     * 数据，枚举配置输出
     *
     * @param data  数据
     * @param value 枚举
     */
    @SuppressWarnings("unchecked")
    public JsonResult(Object data, JsonResultMsg value) {
        this.jsonResult(data, value);
    }


    /**
     * 分页，枚举配置输出
     *
     * @param data  数据
     * @param total 分页
     * @param value 枚举
     */
    public JsonResult(Object data, int total, JsonResultMsg value) {
        HashMap<String, Object> h = new HashMap<>(16);
        h.put("total", total);
        h.put("list", data);
        this.jsonResult(h, value);
    }

    /**
     * 分页，自定义数据名称输出，枚举配置输出
     *
     * @param data     数据
     * @param dataname 数据名称
     * @param total    分页
     * @param value    枚举
     */
    public JsonResult(Object data, String dataname, int total, JsonResultMsg value) {
        HashMap<String, Object> h = new HashMap<>(16);
        h.put("total", total);
        h.put(dataname, data);
        this.jsonResult(h, value);
    }

    /**
     * baidu校验工具使用，校验错误才使用本方法
     *
     * @param ret 百度校验错误返回
     */
    public JsonResult(ComplexResult ret) {
        if (!ret.isSuccess()) {
            if (ret.getErrors() != null && ret.getErrors().size() > 0) {
                this.code = ret.getErrors().get(0).getErrorCode();
                String fieldname = ret.getErrors().get(0).getField();
                this.msg = ret.getErrors().get(0).getErrorMsg() + (fieldname != null ? "，错误字段：" + fieldname : "");
            } else {
                this.code = ERRORCODE;
                this.msg = "";
            }
            this.success = false;
        }
    }

    /**
     * 配置输出
     *
     * @param code
     * @param msg
     * @param success
     */
    public JsonResult(Config<Integer> code, Config<String> msg, Boolean success) {
        this.code = code.get();
        this.msg = msg.get();
        this.success = success;
    }

    private void jsonResult(Object data, JsonResultMsg value) {
        jsonResult(data, value, null);
    }

    /**
     * 构造
     *
     * @param data      数据
     * @param value     继承JsonResultMsg的枚举
     * @param customMsg 自定义文本消息
     */
    private void jsonResult(Object data, JsonResultMsg value, String customMsg) {
        resolveEnum(data, value, customMsg);
    }

    /**
     * 转换枚举
     *
     * @param value
     * @return
     */
    private void resolveEnum(Object data, JsonResultMsg value, String customMsg) {
        Enum e = ((Enum<?>) value);
        this.msgtype = value;
        this.enumtype = e;
        Field f = null;
        try {
            f = e.getClass().getField(e.name());
            JsonResultValue jsonResultValue = f.getAnnotation(JsonResultValue.class);
            if (jsonResultValue != null) {
                Value code = jsonResultValue.Code();
                Value msg = jsonResultValue.Msg();
                String matchercode = p.matcher(code.value()).replaceAll("");
                String matchermsg = p.matcher(msg.value()).replaceAll("");
                Config<Integer> integerConfig = new ConfigUtils(matchercode, -10000).getConfig("Integer", true);
                Config<String> stringConfig = new ConfigUtils(matchermsg, "").getConfig("String", true);
                this.code = integerConfig.get();
                this.data = data;
                this.msg = customMsg == null ? stringConfig.get() : customMsg;
                this.success = value.getSuccess();
            }
        } catch (NoSuchFieldException ex) {
            ex.printStackTrace();
        }
    }
}