package com.linkkou.spring.driven.converters;

import com.linkkou.spring.enums.SystemMsgEnums;

/**
 * 统一输出返回 输出模版
 *
 * @author LK
 * @version 1.0
 * @date 2017-12-08 19:29
 */
public class JsonResultException extends RuntimeException {

    /**
     * 消息枚举类型
     */
    private JsonResultMsg msgtype;


    public JsonResultException() {
        this.msgtype = SystemMsgEnums.OPS_FAILURE;
    }

    public JsonResultException(JsonResultMsg msgtype) {
        this.msgtype = msgtype;
    }

    public JsonResultMsg getMsgtype() {
        return msgtype;
    }
}