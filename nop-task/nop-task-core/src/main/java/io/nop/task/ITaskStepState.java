/**
 * Copyright (c) 2017-2023 Nop Platform. All rights reserved.
 * Author: canonical_entropy@163.com
 * Blog:   https://www.zhihu.com/people/canonical-entropy
 * Gitee:  https://gitee.com/canonical-entropy/nop-chaos
 * Github: https://github.com/entropy-cloud/nop-chaos
 */
package io.nop.task;


import io.nop.core.lang.eval.IEvalScope;

/**
 * state变量相当于是通过闭包方式捕获了所有状态相关的变量集合，从而使得step能够以continuation的方式运行.
 * <p>
 * 持久化状态包含返回结果（正常返回值以及抛出的异常），因此重复执行时发现结果已存在，则直接返回结果，跳过具体执行过程。
 */
public interface ITaskStepState extends ITaskStateCommon {
    /**
     * stepId为静态定义的步骤id. runId为动态执行路径所确定的id. 两者结合在一起唯一确定一个TaskStepState
     *
     * @return
     */
    String getStepId();

    void setStepId(String stepId);

    /**
     * 在TaskInstance范围内动态执行路径所对应的id。例如循环嵌套会生成:3:2这种runId，它标识第一层循环执行到下标为3，第二层循环执行到下标为2
     *
     * @return
     */
    int getRunId();

    void setRunId(int runId);

    String getParentStepId();

    void setParentStepId(String parentStepId);

    String getParentRunId();

    void setParentRunId(String parentRunId);

    String getStepType();

    void setStepType(String stepType);

    Integer getStepStatus();

    void setStepStatus(Integer stepStatus);

    /**
     * 分布式执行时对应执行节点
     *
     * @return
     */
    String getWorkerId();

    void setWorkerId(String workerId);

    /**
     * 步骤所用到的变量。如果把TaskStep看作是普通函数。则这里的vars对应于参数、闭包变量以及函数内部临时变量等所有变量的集合
     *
     * @return
     */
    <T> T getStateBean(Class<T> beanType);

    Object getStateBean();

    void setStateBean(Object stateBean);

    /**
     * 从持久化存储中恢复后调用
     */
    void afterLoad(ITaskContext context);

    void beforeSave(ITaskContext context);

    void succeed(Object result, String nextStepId, ITaskContext context);

    void fail(Throwable exception, ITaskContext context);

    boolean isDone();

    boolean isCompletedSuccessfully();

    boolean needSave();

    TaskStepResult result();

    Throwable exception();

    void exception(Throwable exp);

    IEvalScope getEvalScope();

    void setEvalScope(IEvalScope scope);
}