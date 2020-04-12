package com.linkkou.spring.uid;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author lk
 * @version 1.0
 * @date 2020/4/2 16:02
 */
@Data
@Accessors(chain = true)
public class UidEntity {
    private String UID;
    private String timestamp;
    private String workerId;
    private String sequence;

}
