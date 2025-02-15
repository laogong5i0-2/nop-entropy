/**
 * Copyright (c) 2017-2023 Nop Platform. All rights reserved.
 * Author: canonical_entropy@163.com
 * Blog:   https://www.zhihu.com/people/canonical-entropy
 * Gitee:  https://gitee.com/canonical-entropy/nop-chaos
 * Github: https://github.com/entropy-cloud/nop-chaos
 */
package io.nop.batch.core.consumer;

import io.nop.batch.core.IBatchChunkContext;
import io.nop.batch.core.IBatchConsumer;
import io.nop.commons.concurrent.ratelimit.IRateLimiter;

import java.util.List;

public class RateLimitConsumer<R> implements IBatchConsumer<R, IBatchChunkContext> {
    static final long RATE_LIMIT_TIMEOUT = 1000 * 60 * 20L; // 20分钟

    private final IBatchConsumer<R, IBatchChunkContext> consumer;
    private final IRateLimiter rateLimiter;

    public RateLimitConsumer(IBatchConsumer<R, IBatchChunkContext> consumer, IRateLimiter rateLimiter) {
        this.consumer = consumer;
        this.rateLimiter = rateLimiter;
    }

    @Override
    public void consume(List<R> items, IBatchChunkContext chunkContext) {
        rateLimiter.tryAcquire(chunkContext.getChunkItems().size(), RATE_LIMIT_TIMEOUT);
        consumer.consume(items, chunkContext);
    }
}