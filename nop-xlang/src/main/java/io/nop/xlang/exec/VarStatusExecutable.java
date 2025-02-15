/**
 * Copyright (c) 2017-2023 Nop Platform. All rights reserved.
 * Author: canonical_entropy@163.com
 * Blog:   https://www.zhihu.com/people/canonical-entropy
 * Gitee:  https://gitee.com/canonical-entropy/nop-chaos
 * Github: https://github.com/entropy-cloud/nop-chaos
 */
package io.nop.xlang.exec;

import io.nop.api.core.exceptions.NopException;
import io.nop.api.core.util.Guard;
import io.nop.api.core.util.SourceLocation;
import io.nop.commons.collections.iterator.LoopVarStatus;
import io.nop.commons.util.CollectionHelper;
import io.nop.core.lang.eval.IEvalScope;
import io.nop.core.lang.eval.IExecutableExpression;
import io.nop.core.lang.eval.IExpressionExecutor;

import java.util.Iterator;

/**
 * 将itemsExpr返回的iterator包装为LoopVarStatus对象，并在上下文中设置varStatus变量
 */
public class VarStatusExecutable extends AbstractExecutable {
    private final String varStatusName;
    private final int varStatusSlot;
    private final IExecutableExpression itemsExpr;

    public VarStatusExecutable(SourceLocation loc, String varStatusName, int varStatusSlot,
                               IExecutableExpression itemsExpr) {
        super(loc);
        this.varStatusName = Guard.notEmpty(varStatusName, "varStatusName is empty");
        this.varStatusSlot = varStatusSlot;
        this.itemsExpr = Guard.notNull(itemsExpr, "itemsExpr is null");
    }

    @Override
    public boolean allowBreakPoint() {
        return false;
    }

    @Override
    public void display(StringBuilder sb) {
        sb.append(varStatusName);
    }

    @Override
    public Object execute(IExpressionExecutor executor, IEvalScope scope) {
        Object items = executor.execute(itemsExpr, scope);
        if (items == null)
            return null;

        Iterator<Object> it = toIterator(items);
        LoopVarStatus vs = new LoopVarStatus(it, true);
        scope.getCurrentFrame().setStackValue(varStatusSlot, vs);
        return vs;
    }

    private Iterator<Object> toIterator(Object o) {
        try {
            return CollectionHelper.toIterator(o, false);
        } catch (NopException e) {
            e.addXplStack(itemsExpr);
            throw e;
        }
    }
}
