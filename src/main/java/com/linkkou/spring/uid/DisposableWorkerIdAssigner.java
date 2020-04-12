package com.linkkou.spring.uid;

import com.linkkou.uid.impl.DefaultUidGenerator;
import com.linkkou.uid.worker.WorkerIdAssigner;

import javax.validation.constraints.NotNull;

/**
 * @author lk
 * @version 1.0
 * @date 2020/4/2 14:55
 */
public class DisposableWorkerIdAssigner implements WorkerIdAssigner {

    private long WorkerId;

    public void setWorkerId(@NotNull long workerId) {
        WorkerId = workerId;
    }

    /**
     * Assign worker id for {@link DefaultUidGenerator}
     *
     * @return assigned worker id
     */
    @Override
    public long assignWorkerId() {
        return this.WorkerId;
    }
}
