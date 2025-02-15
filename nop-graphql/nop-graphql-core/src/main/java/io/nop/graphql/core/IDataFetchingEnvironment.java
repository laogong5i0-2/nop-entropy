/**
 * Copyright (c) 2017-2023 Nop Platform. All rights reserved.
 * Author: canonical_entropy@163.com
 * Blog:   https://www.zhihu.com/people/canonical-entropy
 * Gitee:  https://gitee.com/canonical-entropy/nop-chaos
 * Github: https://github.com/entropy-cloud/nop-chaos
 */
package io.nop.graphql.core;

import io.nop.api.core.beans.FieldSelectionBean;
import io.nop.api.core.context.IContext;
import io.nop.api.core.exceptions.NopException;
import io.nop.commons.cache.ICache;
import io.nop.core.lang.eval.IEvalScope;
import io.nop.core.lang.json.JsonTool;
import io.nop.core.reflect.bean.BeanTool;
import io.nop.graphql.core.ast.GraphQLFieldSelection;
import io.nop.graphql.core.ast.GraphQLObjectDefinition;

import java.util.Map;

import static io.nop.graphql.core.GraphQLErrors.ARG_ACTION_NAME;
import static io.nop.graphql.core.GraphQLErrors.ARG_ARG_NAME;
import static io.nop.graphql.core.GraphQLErrors.ARG_EXPECTED_TYPE;
import static io.nop.graphql.core.GraphQLErrors.ERR_GRAPHQL_INVALID_ARG_TYPE;

public interface IDataFetchingEnvironment {
    Object getRoot();

    Object getSource();

    Object getOpRequest();

    IDataFetchingEnvironment copy();

    default Object getArgs() {
        Object req = getOpRequest();
        if (req != null)
            return req;
        Object args = getSelectionBean().getArgs();
        return args;
    }

    default Object getArg(String name) {
        Object args = getArgs();
        if (args == null)
            return null;
        if (args instanceof Map)
            return ((Map<?, ?>) args).get(name);
        return BeanTool.instance().getProperty(args, name);
    }

    default <T> T getArgAsBean(String name, Class<T> beanClass) {
        Object value = getArg(name);
        if (value == null)
            return null;
        if (beanClass.isInstance(value))
            return (T) value;
        if (value instanceof Map)
            return BeanTool.castBeanToType(value, beanClass);
        if (value instanceof String)
            return (T) JsonTool.parseBeanFromText(((String) value).toLowerCase(), beanClass);
        throw new NopException(ERR_GRAPHQL_INVALID_ARG_TYPE).source(getSelection())
                .param(ARG_ACTION_NAME, getSelection().getName()).param(ARG_ARG_NAME, name)
                .param(ARG_EXPECTED_TYPE, beanClass);
    }

    default String getObjName() {
        GraphQLObjectDefinition obj = (GraphQLObjectDefinition) getSelection().getASTParent();
        return obj == null ? null : obj.getName();
    }

    GraphQLFieldSelection getSelection();

    FieldSelectionBean getSelectionBean();

    IGraphQLExecutionContext getExecutionContext();

    default IContext getContext() {
        return getExecutionContext().getContext();
    }

    default ICache<Object, Object> getCache() {
        return getExecutionContext().getCache();
    }

    default IEvalScope getEvalScope() {
        return getExecutionContext().getEvalScope();
    }

    boolean isAsync();
}