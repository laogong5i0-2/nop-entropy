/**
 * Copyright (c) 2017-2023 Nop Platform. All rights reserved.
 * Author: canonical_entropy@163.com
 * Blog:   https://www.zhihu.com/people/canonical-entropy
 * Gitee:  https://gitee.com/canonical-entropy/nop-chaos
 * Github: https://github.com/entropy-cloud/nop-chaos
 */
package io.nop.tcc.core.meta;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.nop.api.core.annotations.data.DataBean;

import java.io.Serializable;

@DataBean
public class TccMethodMeta implements Serializable {
    private final String txnGroup;
    private final String serviceMethod;
    private final String confirmMethod;
    private final String cancelMethod;

    public TccMethodMeta(@JsonProperty("txnGroup") String txnGroup, @JsonProperty("serviceMethod") String serviceMethod,
                         @JsonProperty("confirmMethod") String confirmMethod, @JsonProperty("cancelMethod") String cancelMethod) {
        this.txnGroup = txnGroup;
        this.serviceMethod = serviceMethod;
        this.confirmMethod = confirmMethod;
        this.cancelMethod = cancelMethod;
    }

    public String getTxnGroup() {
        return txnGroup;
    }

    public String getServiceMethod() {
        return serviceMethod;
    }

    public String getConfirmMethod() {
        return confirmMethod;
    }

    public String getCancelMethod() {
        return cancelMethod;
    }
}
