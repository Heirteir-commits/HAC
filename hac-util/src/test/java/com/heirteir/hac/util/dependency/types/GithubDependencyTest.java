package com.heirteir.hac.util.dependency.types;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import com.heirteir.hac.util.dependency.DependencyUtils;
import com.heirteir.hac.util.dependency.plugin.DependencyPluginTest;
import com.heirteir.hac.util.dependency.types.annotation.Github;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;

@Github(fileName = "HAC.Core", pluginName = "HAC-Core", githubRepoRelativeURL = "heirteir-commits/HAC", spigotId = 75180)
public class GithubDependencyTest {
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
    public void testNeedsUpdate() {
        GithubDependency githubDependency = new GithubDependency(plugin, GithubDependencyTest.class.getAnnotation(Github.class));

        this.checkExists(githubDependency);

        Assertions.assertEquals("HAC-Core", githubDependency.getName());
        Assertions.assertEquals("HAC-Core", githubDependency.toString());

        githubDependency = new GithubDependency(plugin, "Dependency-Plugin", "Dependency-Plugin", "heirteir-commits/HAC", 75180, true, false);

        this.checkExists(githubDependency);

        Assertions.assertEquals("Dependency-Plugin", githubDependency.getName());
        Assertions.assertEquals("Dependency-Plugin", githubDependency.toString());
    }

    private void checkExists(GithubDependency dependency) {
        boolean needsUpdate = dependency.needsUpdate();
        boolean download = DependencyUtils.downloadDependency(plugin, dependency);

        Assertions.assertTrue(Files.exists(dependency.getDownloadLocation()));

        try {
            Files.deleteIfExists(dependency.getDownloadLocation());
        } catch (IOException e) {
            plugin.getLog().severe(e);
        }

        Assertions.assertTrue(needsUpdate);
        Assertions.assertTrue(download);
    }
}
