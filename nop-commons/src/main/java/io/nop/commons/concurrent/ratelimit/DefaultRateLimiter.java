/**
 * Copyright (c) 2017-2023 Nop Platform. All rights reserved.
 * Author: canonical_entropy@163.com
 * Blog:   https://www.zhihu.com/people/canonical-entropy
 * Gitee:  https://gitee.com/canonical-entropy/nop-chaos
 * Github: https://github.com/entropy-cloud/nop-chaos
 */
package io.nop.commons.concurrent.ratelimit;

import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.TimeUnit;

public class DefaultRateLimiter implements IRateLimiter {
    private final RateLimiter rateLimiter;

    public DefaultRateLimiter(double permitsPerSecond) {
        this.rateLimiter = RateLimiter.create(permitsPerSecond);
    }

    @Override
    public boolean tryAcquire(int permits, long timeout) {
        return rateLimiter.tryAcquire(permits, timeout, TimeUnit.MILLISECONDS);
    }

    public double getRate() {
        return rateLimiter.getRate();
    }

    public void setRate(double permitsPerSecond) {
        rateLimiter.setRate(permitsPerSecond);
    }

    @Override
    public void release(int permits, long duration, Throwable exception) {
    }
}