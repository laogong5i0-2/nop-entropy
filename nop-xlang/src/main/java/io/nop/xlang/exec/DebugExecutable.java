/**
 * Copyright (c) 2017-2023 Nop Platform. All rights reserved.
 * Author: canonical_entropy@163.com
 * Blog:   https://www.zhihu.com/people/canonical-entropy
 * Gitee:  https://gitee.com/canonical-entropy/nop-chaos
 * Github: https://github.com/entropy-cloud/nop-chaos
 */
package io.nop.xlang.exec;

import io.nop.api.core.convert.ConvertHelper;
import io.nop.api.core.util.Guard;
import io.nop.api.core.util.SourceLocation;
import io.nop.core.lang.eval.IEvalScope;
import io.nop.core.lang.eval.IExecutableExpression;
import io.nop.core.lang.eval.IExpressionExecutor;
import io.nop.xlang.utils.DebugHelper;

public class DebugExecutable extends AbstractExecutable {
    private final IExecutableExpression valueExpr;
    private final IExecutableExpression prefixExpr;

    public DebugExecutable(SourceLocation loc, IExecutableExpression valueExpr, IExecutableExpression prefixExpr) {
        super(loc);
        this.valueExpr = Guard.notNull(valueExpr, "valueExpr");
        this.prefixExpr = Guard.notNull(prefixExpr, "prefixExpr");
    }

    @Override
    public Object execute(IExpressionExecutor executor, IEvalScope scope) {
        Object v = executor.execute(valueExpr, scope);
        // nameExpr在valueExpr之后执行，原代码结构为value.$(name)这种形式
        String prefix = ConvertHelper.toString(executor.execute(prefixExpr, scope));
        return DebugHelper.v(getLocation(), prefix, valueExpr.display(), v);
    }

    @Override
    public void display(StringBuilder sb) {
        valueExpr.display(sb);
        sb.append(".$(");
        prefixExpr.display(sb);
        sb.append(")");
    }
}
