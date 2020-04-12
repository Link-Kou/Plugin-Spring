package com.linkkou.spring.uid.impl;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.linkkou.gson.processor.GsonAutowired;
import com.linkkou.spring.uid.Uid;
import com.linkkou.spring.uid.UidEntity;
import com.linkkou.uid.UidGenerator;
import com.linkkou.uid.exception.UidGenerateException;

import javax.validation.constraints.NotNull;


/**
 * @author lk
 * @version 1.0
 * @date 2020/4/2 14:55
 */
public class UidImpl implements Uid {

    @GsonAutowired
    private Gson gson;

    private UidGenerator uidGenerator;

    public void setUidGenerator(@NotNull UidGenerator uidGenerator) {
        this.uidGenerator = uidGenerator;
    }

    /**
     * 获取到文本
     *
     * @return String
     * @throws UidGenerateException
     */
    @Override
    public String getUid() throws UidGenerateException, JsonSyntaxException {
        final UidEntity uidEntity = get();
        return uidEntity.getUID();
    }

    /**
     * 获取对象类型
     *
     * @return UidEntity
     * @throws UidGenerateException
     */
    @Override
    public UidEntity get() throws UidGenerateException, JsonSyntaxException {
        final long uid = uidGenerator.getUID();
        final String s = uidGenerator.parseUID(uid);
        return gson.fromJson(s, UidEntity.class);
    }

    @Override
    public UidGenerator getUidGenerator() {
        return this.uidGenerator;
    }
}
