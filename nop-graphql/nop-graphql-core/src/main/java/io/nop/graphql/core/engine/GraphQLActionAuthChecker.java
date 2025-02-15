/**
 * Copyright (c) 2017-2023 Nop Platform. All rights reserved.
 * Author: canonical_entropy@163.com
 * Blog:   https://www.zhihu.com/people/canonical-entropy
 * Gitee:  https://gitee.com/canonical-entropy/nop-chaos
 * Github: https://github.com/entropy-cloud/nop-chaos
 */
package io.nop.graphql.core.engine;

import io.nop.api.core.auth.ActionAuthMeta;
import io.nop.api.core.auth.IActionAuthChecker;
import io.nop.api.core.auth.IUserContext;
import io.nop.api.core.beans.FieldSelectionBean;
import io.nop.api.core.exceptions.NopException;
import io.nop.auth.api.AuthApiErrors;
import io.nop.core.context.IServiceContext;
import io.nop.graphql.core.IGraphQLExecutionContext;
import io.nop.graphql.core.ast.GraphQLFieldSelection;
import io.nop.graphql.core.ast.GraphQLFragmentSelection;
import io.nop.graphql.core.ast.GraphQLOperation;
import io.nop.graphql.core.ast.GraphQLSelection;
import io.nop.graphql.core.ast.GraphQLSelectionSet;

import static io.nop.auth.api.AuthApiErrors.ARG_OBJ_TYPE_NAME;
import static io.nop.auth.api.AuthApiErrors.ARG_PERMISSION;
import static io.nop.auth.api.AuthApiErrors.ARG_ROLES;

public class GraphQLActionAuthChecker {
    // static final Logger LOG = LoggerFactory.getLogger(GraphQLActionAuthChecker.class);

    public static GraphQLActionAuthChecker INSTANCE = new GraphQLActionAuthChecker();

    public void check(IGraphQLExecutionContext context) {
        IUserContext userContext = context.getUserContext();
        if (userContext == null)
            return;

        IActionAuthChecker checker = context.getActionAuthChecker();
        if (checker == null)
            return;

        GraphQLOperation operation = context.getOperation();
        FieldSelectionBean selectionBean = context.getFieldSelection();

        String objTypeName = operation.getSelectionSet().getObjTypeName();

        for (GraphQLSelection field : operation.getSelectionSet().getSelections()) {
            GraphQLFieldSelection selection = (GraphQLFieldSelection) field;
            FieldSelectionBean subSelection = selectionBean.getField(selection.getAliasOrName());
            if (subSelection == null)
                continue;

            checkAuth(objTypeName, selection, checker, context, true);

            checkSelectionSet(selection.getSelectionSet(), subSelection, checker, userContext, context);
        }
    }

    void checkSelectionSet(GraphQLSelectionSet selectionSet, FieldSelectionBean selectionBean,
                           IActionAuthChecker checker, IUserContext userContext, IGraphQLExecutionContext context) {
        if (selectionSet == null)
            return;

        String objTypeName = selectionSet.getObjTypeName();

        for (GraphQLSelection selection : selectionSet.getSelections()) {
            if (selection instanceof GraphQLFragmentSelection) {
                GraphQLFragmentSelection fragmentSelection = (GraphQLFragmentSelection) selection;
                checkSelectionSet(fragmentSelection.getResolvedFragment().getSelectionSet(), selectionBean, checker,
                        userContext, context);
            } else {
                GraphQLFieldSelection fieldSelection = (GraphQLFieldSelection) selection;
                FieldSelectionBean subSelection = selectionBean.getField(fieldSelection.getAliasOrName());
                if (subSelection == null)
                    continue;

                checkAuth(objTypeName, fieldSelection, checker, context, false);

                checkSelectionSet(fieldSelection.getSelectionSet(), subSelection, checker, userContext, context);
            }
        }
    }

    public static void checkAuth(String objTypeName, GraphQLFieldSelection fieldSelection, IActionAuthChecker checker,
                                 IServiceContext context, boolean action) {
        checkAuth(objTypeName, fieldSelection.getName(), fieldSelection.getFieldDefinition().getAuth(), checker, context, action);
    }

    public static void checkAuth(String objTypeName, String fieldName, ActionAuthMeta auth, IActionAuthChecker checker,
                                 IServiceContext context, boolean action) {
        if (checker == null)
            return;

        if (auth == null)
            return;

        // 如果是公开方法，则不检查用户权限
        if (auth.isPublicAccess())
            return;

        IUserContext userContext = context.getUserContext();
        if (userContext == null)
            throw new IllegalStateException("nop.err.auth.no-user-context");
        if (auth.getRoles() != null && !auth.getRoles().isEmpty()) {
            if (userContext.isUserInAnyRole(auth.getRoles()))
                return;
        }

        if (auth.getPermissions() != null && !auth.getPermissions().isEmpty()) {
            if (checker.isPermissionSetSatisfied(auth.getPermissions(), context))
                return;
        }

        if (action) {
            throw new NopException(AuthApiErrors.ERR_AUTH_NO_PERMISSION)
                    .param(AuthApiErrors.ARG_ACTION_NAME, fieldName)
                    .param(ARG_PERMISSION, auth.getPermissions())
                    .param(ARG_ROLES, auth.getRoles())
                    .param(ARG_OBJ_TYPE_NAME, objTypeName);
        } else {
            throw new NopException(AuthApiErrors.ERR_AUTH_NO_PERMISSION_FOR_FIELD)
                    .param(AuthApiErrors.ARG_FIELD_NAME, fieldName)
                    .param(ARG_PERMISSION, auth.getPermissions())
                    .param(ARG_ROLES, auth.getRoles())
                    .param(ARG_OBJ_TYPE_NAME, objTypeName);
        }
    }
}