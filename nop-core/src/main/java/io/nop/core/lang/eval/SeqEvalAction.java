/**
 * Copyright (c) 2017-2023 Nop Platform. All rights reserved.
 * Author: canonical_entropy@163.com
 * Blog:   https://www.zhihu.com/people/canonical-entropy
 * Gitee:  https://gitee.com/canonical-entropy/nop-chaos
 * Github: https://github.com/entropy-cloud/nop-chaos
 */
package io.nop.core.lang.eval;

import io.nop.core.context.IEvalContext;

public class SeqEvalAction implements IEvalAction {
    private final IEvalAction[] actions;

    public SeqEvalAction(IEvalAction... actions) {
        this.actions = actions;
    }

    @Override
    public Object invoke(IEvalContext ctx) {
        Object ret = null;
        for (IEvalAction action : actions) {
            ret = action.invoke(ctx);
        }
        return ret;
    }
}