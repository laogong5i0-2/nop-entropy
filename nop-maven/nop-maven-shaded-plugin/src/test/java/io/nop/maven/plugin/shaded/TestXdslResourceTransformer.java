package io.nop.maven.plugin.shaded;

import io.nop.core.initialize.CoreInitialization;
import io.nop.core.resource.IResource;
import io.nop.core.resource.VirtualFileSystem;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

public class TestXdslResourceTransformer {
    @BeforeAll
    public static void init() {
        CoreInitialization.initialize();
    }

    @AfterAll
    public static void destroy() {
        CoreInitialization.destroy();
    }

    @Test
    public void testTransform() throws IOException {
        IResource resource = VirtualFileSystem.instance().getResource("/nop/auth/xlib/test.xlib");
        new XdslResourceTransformer().processResource(resource, new ArrayList<>());
    }

    @Test
    public void testTransformXpl() throws IOException {
        IResource resource = VirtualFileSystem.instance().getResource("/nop/auth/model/test.xbiz");
        new XdslResourceTransformer().processResource(resource, new ArrayList<>());
    }
}
