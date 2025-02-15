/**
 * Copyright (c) 2017-2023 Nop Platform. All rights reserved.
 * Author: canonical_entropy@163.com
 * Blog:   https://www.zhihu.com/people/canonical-entropy
 * Gitee:  https://gitee.com/canonical-entropy/nop-chaos
 * Github: https://github.com/entropy-cloud/nop-chaos
 */
package io.nop.xlang.ast;

import io.nop.api.core.util.SourceLocation;
import io.nop.xlang.ast._gen._LogicalExpression;

public class LogicalExpression extends _LogicalExpression {

    public static LogicalExpression valueOf(SourceLocation loc, XLangOperator op, Expression left, Expression right) {
        LogicalExpression node = new LogicalExpression();
        node.setLocation(loc);
        node.setOperator(op);
        node.setLeft(left);
        node.setRight(right);
        return node;
    }
}