/**
 * Copyright (c) 2017-2023 Nop Platform. All rights reserved.
 * Author: canonical_entropy@163.com
 * Blog:   https://www.zhihu.com/people/canonical-entropy
 * Gitee:  https://gitee.com/canonical-entropy/nop-chaos
 * Github: https://github.com/entropy-cloud/nop-chaos
 */
package io.nop.orm.eql.meta;

import io.nop.dao.dialect.IDialect;
import io.nop.orm.model.OrmEntityFilterModel;

import java.util.List;

public interface ISqlTableMeta extends ISqlSelectionMeta {
    String getEntityName();

    String getQuerySpace();

    ISqlExprMeta getEntityExprMeta();

    boolean isUseLogicalDelete();

    default boolean hasFilter() {
        return getFilters() != null && !getFilters().isEmpty();
    }

    List<OrmEntityFilterModel> getFilters();

    //   ISqlExprMeta getDeleteFlagPropMeta();

    String getDeleteFlagPropName();

    Object getDeleteFlagValue(boolean b, IDialect dialect);
}
