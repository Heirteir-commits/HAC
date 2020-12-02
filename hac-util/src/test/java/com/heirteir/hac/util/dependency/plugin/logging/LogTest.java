package com.heirteir.hac.util.dependency.plugin.logging;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import com.heirteir.hac.util.dependency.plugin.DependencyPluginTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class LogTest {
    private static ServerMock server;
    private static DependencyPluginTest plugin;

    @BeforeAll
    public static void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(DependencyPluginTest.class);
    }

    @AfterAll
    public static void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    void testLog() {
        plugin.getLog().reportFatalError("Testing", false);

        Assertions.assertThrows(IllegalStateException.class, () -> plugin.getLog().open());
    }
}
