/**
 * Copyright (c) 2017-2023 Nop Platform. All rights reserved.
 * Author: canonical_entropy@163.com
 * Blog:   https://www.zhihu.com/people/canonical-entropy
 * Gitee:  https://gitee.com/canonical-entropy/nop-chaos
 * Github: https://github.com/entropy-cloud/nop-chaos
 */
package io.nop.xlang.exec;

import io.nop.core.lang.eval.IEvalScope;
import io.nop.core.lang.eval.IExecutableExpression;
import io.nop.core.lang.eval.IExpressionExecutor;
import io.nop.xlang.utils.ExprEvalHelper;

public class CollectSqlExecutable extends AbstractExecutable {
    private final IExecutableExpression bodyExpr;

    public CollectSqlExecutable(IExecutableExpression bodyExpr) {
        super(bodyExpr.getLocation());
        this.bodyExpr = bodyExpr;
    }

    @Override
    public void display(StringBuilder sb) {
        sb.append("@collect-sql:");
        bodyExpr.display(sb);
    }

    @Override
    public boolean allowBreakPoint() {
        return false;
    }

    @Override
    public Object execute(IExpressionExecutor executor, IEvalScope scope) {
        return ExprEvalHelper.generateSql(ctx -> bodyExpr.execute(executor, scope), scope);
    }
}