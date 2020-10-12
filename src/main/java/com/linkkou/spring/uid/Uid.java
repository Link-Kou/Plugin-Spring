package com.linkkou.spring.uid;

import com.linkkou.uid.UidGenerator;
import com.linkkou.uid.exception.UidGenerateException;
import org.springframework.stereotype.Repository;

/**
 * @author lk
 * @version 1.0
 * @date 2020/4/2 14:51
 */
@Repository
public interface Uid {

    /**
     * 获取到文本
     * @return String
     * @throws UidGenerateException
     */
    String getUid() throws UidGenerateException;

    /**
     * 获取对象类型
     * @return UidEntity
     * @throws UidGenerateException
     */
    UidEntity get() throws UidGenerateException;


    UidGenerator getUidGenerator();

}
