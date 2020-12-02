package com.heretere.hac.util.dependency.types;

import be.seeseemelk.mockbukkit.MockBukkit;
import com.heretere.hac.util.dependency.plugin.DependencyPluginTest;
import com.heretere.hac.util.dependency.types.annotation.Maven;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;

@Maven(groupId = "com.flowpowered", artifactId = "flow-math", version = "1.0.3")
class MavenDependencyTest {
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
    void testNeedsUpdate() {
        MavenDependency mavenDependency = new MavenDependency(plugin, MavenDependencyTest.class.getAnnotation(Maven.class));

        this.checkExists(mavenDependency);

        Assertions.assertEquals("flow-math-1.0.3", mavenDependency.getName());
        Assertions.assertEquals(String.format("%s:%s:%s from %s", "com.flowpowered", "flow-math", "1.0.3", "https://repo1.maven.org/maven2/"), mavenDependency.toString());

        mavenDependency = new MavenDependency(plugin, "org.javassist", "javassist", "3.27.0-GA", "https://repo1.maven.org/maven2");

        this.checkExists(mavenDependency);

        Assertions.assertEquals("javassist-3.27.0-GA", mavenDependency.getName());
        Assertions.assertEquals(String.format("%s:%s:%s from %s", "org.javassist", "javassist", "3.27.0-GA", "https://repo1.maven.org/maven2/"), mavenDependency.toString());
    }

    private void checkExists(MavenDependency dependency) {
        boolean needsUpdate = dependency.needsUpdate();
        boolean download = dependency.download();

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
