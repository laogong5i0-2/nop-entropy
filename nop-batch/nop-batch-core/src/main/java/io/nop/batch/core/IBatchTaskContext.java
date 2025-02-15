/**
 * Copyright (c) 2017-2023 Nop Platform. All rights reserved.
 * Author: canonical_entropy@163.com
 * Blog:   https://www.zhihu.com/people/canonical-entropy
 * Gitee:  https://gitee.com/canonical-entropy/nop-chaos
 * Github: https://github.com/entropy-cloud/nop-chaos
 */
package io.nop.batch.core;

import io.nop.api.core.beans.IntRangeBean;
import io.nop.core.context.IServiceContext;

import java.util.Map;

/**
 * 一个批处理任务对应一个loader + processor + consumer的组合调用
 */
public interface IBatchTaskContext extends IServiceContext {
    String getTaskName();

    String getTaskId();

    /**
     * 外部传入的只读参数，在任务执行过程中不会被修改
     */
    Map<String, Object> getParams();

    default Object getParam(String name) {
        Map<String, Object> params = getParams();
        return params == null ? null : params.get(name);
    }

    /**
     * 持久化的状态变量。当批处理任务失败后重试时可以读取上次处理状态
     */
    Map<String, Object> getPersistVars();

    default Object getPersistVar(String name) {
        Map<String, Object> vars = getPersistVars();
        if (vars == null)
            return null;
        return vars.get(name);
    }

    void setPersistVar(String name, Object value);

    /**
     * 本次任务处理所涉及到的数据分区
     */
    IntRangeBean getPartition();

    void setPartition(IntRangeBean partition);

    boolean isRecoverMode();

    void setRecoverMode(boolean recoverMode);

    IBatchChunkContext newChunkContext();

    IBatchTaskMetrics getMetrics();

    void setMetrics(IBatchTaskMetrics metrics);

    /**
     * 处理过程中因为出错跳过的记录条目数
     */
    long getSkipItemCount();

    void incSkipItemCount(int count);
}