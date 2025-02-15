package io.nop.auth.service;

import io.nop.api.core.annotations.autotest.EnableSnapshot;
import io.nop.api.core.annotations.autotest.NopTestConfig;
import io.nop.api.core.beans.ApiRequest;
import io.nop.autotest.junit.JunitAutoTestCase;
import io.nop.graphql.core.IGraphQLExecutionContext;
import io.nop.graphql.core.engine.IGraphQLEngine;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.Map;

@NopTestConfig(localDb = true)
public class TestSiteMapApi extends JunitAutoTestCase {
    @Inject
    IGraphQLEngine graphQLEngine;

    @EnableSnapshot
    @Test
    public void testRpc(){
        ApiRequest<Map<String,Object>> request = new ApiRequest<>();
        // 测试children自动展开到6层
        IGraphQLExecutionContext context = graphQLEngine.newRpcContext(null,
                "SiteMapApi__getSiteMap", request);
        System.out.print(context.getFieldSelection().toString());
    }
}
