package com.heretere.hac.util.dependency.plugin.logging;

import be.seeseemelk.mockbukkit.MockBukkit;
import com.heretere.hac.util.dependency.plugin.DependencyPluginTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class LogTest {
    private static DependencyPluginTest plugin;

    @BeforeAll
    public static void setUp() {
        MockBukkit.mock();
        plugin = MockBukkit.load(DependencyPluginTest.class);
    }

    @AfterAll
    public static void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    void testLog() {
        Log log = plugin.getLog();
        log.reportFatalError("Testing", false);

        Assertions.assertThrows(IllegalStateException.class, log::open);
    }
}
