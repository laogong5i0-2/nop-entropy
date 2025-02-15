package io.nop.auth.service;

import io.nop.api.core.annotations.autotest.NopTestConfig;
import io.nop.api.core.beans.ApiRequest;
import io.nop.api.core.beans.ApiResponse;
import io.nop.api.core.beans.graphql.GraphQLRequestBean;
import io.nop.api.core.beans.graphql.GraphQLResponseBean;
import io.nop.auth.dao.entity.NopAuthGroup;
import io.nop.autotest.junit.JunitBaseTestCase;
import io.nop.dao.api.IDaoProvider;
import io.nop.dao.api.IEntityDao;
import io.nop.graphql.core.IGraphQLExecutionContext;
import io.nop.graphql.core.engine.IGraphQLEngine;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@NopTestConfig(localDb = true, initDatabaseSchema = true)
public class TestBaseCrud extends JunitBaseTestCase {
    @Inject
    IGraphQLEngine graphQLEngine;

    @Inject
    IDaoProvider daoProvider;

    /**
     * 只定义biz和meta文件，在xbiz根节点上标注graphql:base="crud"
     */
    @Test
    public void testFindPage() {
        GraphQLRequestBean request = new GraphQLRequestBean();
        request.setQuery("query { NopAuthUserEx__findPage{ items{ id, userName } } }");
        IGraphQLExecutionContext context = graphQLEngine.newGraphQLContext(request);
        GraphQLResponseBean response = graphQLEngine.executeGraphQL(context);
        assertTrue(!response.hasError());
    }

    @Test
    public void testActiveFindPage() {
        GraphQLRequestBean request = new GraphQLRequestBean();
        request.setQuery("query { NopAuthUserEx__active_findPage2{ items{ id, userName } } }");
        IGraphQLExecutionContext context = graphQLEngine.newGraphQLContext(request);
        GraphQLResponseBean response = graphQLEngine.executeGraphQL(context);
        assertTrue(!response.hasError());
    }

    @Test
    public void testUpdate() {
        IEntityDao<NopAuthGroup> dao = daoProvider.daoFor(NopAuthGroup.class);
        NopAuthGroup entity = dao.newEntity();
        entity.setName("aa");
        dao.saveEntity(entity);

        ApiRequest<Map<String, Object>> request = new ApiRequest<>();
        Map<String, Object> data = Map.of("id", entity.get_id(), "name", "bb");
        request.setData(Map.of("data", data));
        IGraphQLExecutionContext context = graphQLEngine.newRpcContext(null, "NopAuthGroup__update", request);
        ApiResponse<?> response = graphQLEngine.executeRpc(context);
        assertTrue(response.isOk());

        entity = dao.getEntityById(entity.get_id());
        assertEquals("bb", entity.getName());
    }

}
