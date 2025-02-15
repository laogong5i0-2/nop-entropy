/**
 * Copyright (c) 2017-2023 Nop Platform. All rights reserved.
 * Author: canonical_entropy@163.com
 * Blog:   https://www.zhihu.com/people/canonical-entropy
 * Gitee:  https://gitee.com/canonical-entropy/nop-chaos
 * Github: https://github.com/entropy-cloud/nop-chaos
 */
package io.nop.orm.tdengine.message;

import io.nop.api.core.message.IMessageConsumer;
import io.nop.api.core.message.IMessageService;
import io.nop.api.core.message.IMessageSubscription;
import io.nop.api.core.message.MessageSendOptions;
import io.nop.api.core.message.MessageSubscribeOptions;

import java.util.concurrent.CompletionStage;

public class TdMessageService implements IMessageService {
    @Override
    public CompletionStage<Void> sendAsync(String topic, Object message, MessageSendOptions options) {
        return null;
    }

    @Override
    public IMessageSubscription subscribe(String topic, IMessageConsumer listener, MessageSubscribeOptions options) {
        return null;
    }
}
