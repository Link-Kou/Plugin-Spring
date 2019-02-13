package com.plugin.javawidget.driven.converters;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.plugin.configproperty.Config;
import com.plugin.configproperty.ConfigUtils;
import com.plugin.configproperty.ConfigValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.swing.text.html.Option;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Control 输出模版
 *
 * @author LK
 * @version 1.0
 * @date 2017-12-08 19:29
 */
public class JsonResult<T> {

    private static Logger logger = LoggerFactory.getLogger("JsonResultLogger");

    @ConfigValue(@Value("${SystemMsgEnums.ERROR.JSONRESULT.NOSUCHFIELDEXCEPTION.msg}"))
    private transient Config<String> NOSUCHFIELDEXCEPTION;


    private static final String CODENAME = "code";
    private static final String MSGNAME = "msg";

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
    private transient JsonResultMsg<Enum> msgtype;

    /**
     * 消息枚举原生类型
     */
    private transient Enum<?> enumtype;

    /**
     * 数据类型
     */
    private T data;

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
    public JsonResultMsg<Enum> getMsgtype() {
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
    @Deprecated
    public boolean getSuccess() {
        return success;
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
     * @return E
     */
    public T getData() {
        return this.data;
    }

    /**
     * 获取HashMap格式
     * @return
     */
    public Map<String, Object> getMap(){
        return resolveEnum(this.msgtype);
    }

    /**
     * 比较枚举是否一致
     * <p>注意：枚举继承 {@code JsonResultMsg<Enum>}正确</p>
     * 注意：枚举继承 {@code JsonResultMsg<Enum<?>>} 错误
     * @param value
     * @return
     */
    public boolean isEnums(JsonResultMsg<Enum> value) {
        final HashMap<String, Object> hashMap = resolveEnum(value);
        if (this.code.equals((Integer) hashMap.get("code")) && this.msg.equals((String) hashMap.get("msg"))) {
            return true;
        }
        return false;
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
    public JsonResult(JsonResultMsg<Enum> value) {
        this.jsonResult(null, value);
    }

    /**
     * 枚举配置输出,自定义Msg输出
     * <br/>
     * <b>警告：此方法用于与HTTP请求或其他请求输出远程服务</b>
     *
     * @param value 枚举
     */
    @SuppressWarnings("unchecked")
    public JsonResult(JsonResultMsg<Enum> value, String msg) {
        this.jsonResult(null, value, msg);
    }

    /**
     * 数据，枚举配置输出
     *
     * @param data  数据
     * @param value 枚举
     */
    @SuppressWarnings("unchecked")
    public JsonResult(Object data, JsonResultMsg<Enum> value) {
        this.jsonResult(data, value);
    }


    /**
     * 分页，枚举配置输出
     *
     * @param data  数据
     * @param total 分页
     * @param value 枚举
     */
    @Deprecated
    public JsonResult(Object data, int total, JsonResultMsg<Enum> value) {
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
    @Deprecated
    public JsonResult(Object data, String dataname, int total, JsonResultMsg<Enum> value) {
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

    private static Pattern p = Pattern.compile("[\\$\\{\\}]");

    private void jsonResult(Object data, JsonResultMsg<Enum> value) {
        jsonResult(data, value, null);
    }

    private void jsonResult(Object data, JsonResultMsg<Enum> value, String customMsg) {
        final HashMap<String, Object> hashMap = resolveEnum(value);
        this.code = (Integer) hashMap.get(CODENAME);
        this.data = (T) data;
        this.msg = customMsg == null ? (String) hashMap.get(MSGNAME) : customMsg;
        this.success = value.getSuccess();
    }

    /**
     * 转换枚举
     * @param value
     * @return
     */
    private HashMap<String, Object> resolveEnum(JsonResultMsg<Enum> value) {
        Enum e = ((Enum<?>) value);
        this.msgtype = value;
        this.enumtype = e;
        Field f = null;
        try {
            f = e.getClass().getField(e.name());
        } catch (NoSuchFieldException e1) {
            logger.error(NOSUCHFIELDEXCEPTION.get());
            throw new SecurityException(NOSUCHFIELDEXCEPTION.get());
        }
        JsonResultValue jsonResultValue = f.getAnnotation(JsonResultValue.class);
        HashMap<String, Object> hashMap = new HashMap<>(16);
        if (jsonResultValue != null) {
            Value code = jsonResultValue.Code();
            Value msg = jsonResultValue.Msg();
            String matchercode = p.matcher(code.value()).replaceAll("");
            String matchermsg = p.matcher(msg.value()).replaceAll("");
            hashMap.put(CODENAME, new ConfigUtils(matchercode).getInteger());
            hashMap.put(MSGNAME, new ConfigUtils(matchermsg).getString());
            return hashMap;
        }else {
            return hashMap;
        }
    }
}