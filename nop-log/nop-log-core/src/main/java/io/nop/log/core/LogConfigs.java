/**
 * Copyright (c) 2017-2023 Nop Platform. All rights reserved.
 * Author: canonical_entropy@163.com
 * Blog:   https://www.zhihu.com/people/canonical-entropy
 * Gitee:  https://gitee.com/canonical-entropy/nop-chaos
 * Github: https://github.com/entropy-cloud/nop-chaos
 */
package io.nop.log.core;

import io.nop.api.core.annotations.core.Description;
import io.nop.api.core.config.IConfigReference;
import io.nop.api.core.util.SourceLocation;

import static io.nop.api.core.config.AppConfig.varRef;

public interface LogConfigs {
    SourceLocation s_loc = SourceLocation.fromClass(LogConfigs.class);
    @Description("设置全局的log级别")
    IConfigReference<String> CFG_LOG_LEVEL = varRef(s_loc, "nop.log.level", String.class, null);

}
