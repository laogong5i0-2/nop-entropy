/**
 * Copyright (c) 2017-2023 Nop Platform. All rights reserved.
 * Author: canonical_entropy@163.com
 * Blog:   https://www.zhihu.com/people/canonical-entropy
 * Gitee:  https://gitee.com/canonical-entropy/nop-chaos
 * Github: https://github.com/entropy-cloud/nop-chaos
 */
package io.nop.xlang.exec;

import io.nop.api.core.util.Guard;
import io.nop.api.core.util.SourceLocation;
import io.nop.commons.util.CollectionHelper;
import io.nop.commons.util.StringHelper;
import io.nop.commons.util.objects.ValueWithLocation;
import io.nop.core.lang.eval.IEvalScope;
import io.nop.core.lang.eval.IExecutableExpression;
import io.nop.core.lang.eval.IExpressionExecutor;
import io.nop.core.lang.xml.IXNodeHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.nop.xlang.XLangErrors.ARG_TAG_NAME;
import static io.nop.xlang.XLangErrors.ARG_TAG_NAME_EXPR;
import static io.nop.xlang.XLangErrors.ERR_EXEC_XML_EXT_ATTRS_NOT_MAP;
import static io.nop.xlang.XLangErrors.ERR_XPL_DISALLOW_OUTPUT_INVALID_XML_NAME;

public class GenNodeExecutable extends AbstractExecutable {
    private final IExecutableExpression tagNameExpr;
    private final String tagName;
    private final GenNodeAttrExecutable[] attrExprs;
    private final IExecutableExpression extAttrs;
    private final IExecutableExpression bodyExpr;
    private final Set<String> attrNames;

    public GenNodeExecutable(SourceLocation loc, String tagName, IExecutableExpression tagNameExpr,
                             GenNodeAttrExecutable[] attrExprs, IExecutableExpression extAttrs, IExecutableExpression bodyExpr) {
        super(loc);
        Guard.checkArgument(tagName != null || tagNameExpr != null, "empty tagName");
        this.tagNameExpr = tagNameExpr;
        this.tagName = tagName;
        this.attrExprs = Guard.notNull(attrExprs, "attrExprs");
        this.bodyExpr = bodyExpr;
        this.extAttrs = extAttrs;
        this.attrNames = buildAttrNames(attrExprs, extAttrs);
    }

    private static Set<String> buildAttrNames(GenNodeAttrExecutable[] attrExprs, IExecutableExpression extAttrs) {
        if (extAttrs == null)
            return null;
        List<String> list = new ArrayList<>(attrExprs.length);
        for (GenNodeAttrExecutable attrExpr : attrExprs) {
            list.add(attrExpr.getName());
        }
        return CollectionHelper.immutableSet(list);
    }

    @Override
    public void display(StringBuilder sb) {
        sb.append("@node:").append(tagName);
    }

    @Override
    public Object execute(IExpressionExecutor executor, IEvalScope scope) {
        IXNodeHandler out = (IXNodeHandler) scope.getOut();
        SourceLocation loc = getLocation();
        Map<String, ValueWithLocation> attrs = buildAttrs(executor, scope);

        String tagName = buildTagName(executor, scope);
        if (bodyExpr == null) {
            out.simpleNode(loc, tagName, attrs);
        } else {
            out.beginNode(loc, tagName, attrs);
            executor.execute(bodyExpr, scope);
            out.endNode(tagName);
        }
        return null;
    }

    String buildTagName(IExpressionExecutor executor, IEvalScope scope) {
        if (tagName != null)
            return tagName;

        Object value = executor.execute(tagNameExpr, scope);
        if (value instanceof String) {
            String str = value.toString();
            if (!StringHelper.isValidXmlName(str))
                throw newError(ERR_XPL_DISALLOW_OUTPUT_INVALID_XML_NAME).param(ARG_TAG_NAME, str)
                        .param(ARG_TAG_NAME_EXPR, tagNameExpr);
            return str;
        }

        throw newError(ERR_XPL_DISALLOW_OUTPUT_INVALID_XML_NAME).param(ARG_TAG_NAME, value).param(ARG_TAG_NAME_EXPR,
                tagNameExpr);
    }

    private Map<String, ValueWithLocation> buildAttrs(IExpressionExecutor executor, IEvalScope scope) {
        if (attrExprs.length == 0 && extAttrs == null) {
            return Collections.emptyMap();
        }

        Map<String, ValueWithLocation> map = new LinkedHashMap<>();
        for (GenNodeAttrExecutable attrExpr : attrExprs) {
            IExecutableExpression valueExpr = attrExpr.getValueExpr();
            Object value = executor.execute(valueExpr, scope);
            if (value == null)
                continue;
            map.put(attrExpr.getName(), ValueWithLocation.of(valueExpr.getLocation(), value));
        }

        if (extAttrs != null) {
            Object value = executor.execute(extAttrs, scope);
            if (value != null) {
                if (!(value instanceof Map))
                    throw newError(ERR_EXEC_XML_EXT_ATTRS_NOT_MAP).loc(extAttrs.getLocation());

                Map<String, Object> extMap = (Map<String, Object>) value;
                for (Map.Entry<String, Object> entry : extMap.entrySet()) {
                    Object extValue = entry.getValue();
                    if (extValue == null)
                        continue;

                    if (attrNames.contains(entry.getKey()))
                        continue;

                    map.put(entry.getKey(), ValueWithLocation.of(extAttrs.getLocation(), extValue));
                }
            }
        }

        return map;
    }
}
