/**
 * Copyright (c) 2017-2023 Nop Platform. All rights reserved.
 * Author: canonical_entropy@163.com
 * Blog:   https://www.zhihu.com/people/canonical-entropy
 * Gitee:  https://gitee.com/canonical-entropy/nop-chaos
 * Github: https://github.com/entropy-cloud/nop-chaos
 */
package io.nop.xlang.ast;

import io.nop.core.type.IGenericType;

public class XLangTypeHelper {
    public static NamedTypeNode buildTypeNode(IGenericType type) {
        if (type == null)
            return null;
        TypeNameNode node = new TypeNameNode();
        node.setTypeInfo(type);
        node.setTypeName(type.toString());
        return node;
    }
}
