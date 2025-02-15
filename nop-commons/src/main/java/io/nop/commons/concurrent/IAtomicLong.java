/**
 * Copyright (c) 2017-2023 Nop Platform. All rights reserved.
 * Author: canonical_entropy@163.com
 * Blog:   https://www.zhihu.com/people/canonical-entropy
 * Gitee:  https://gitee.com/canonical-entropy/nop-chaos
 * Github: https://github.com/entropy-cloud/nop-chaos
 */
package io.nop.commons.concurrent;

public interface IAtomicLong {
    long get();

    void set(long value);

    long getAndSet(long newValue);

    boolean compareAndSet(long expect, long update);

    long getAndIncrement();

    long getAndDecrement();

    long getAndAdd(long delta);

    long incrementAndGet();

    long decrementAndGet();

    long addAndGet(long delta);
}