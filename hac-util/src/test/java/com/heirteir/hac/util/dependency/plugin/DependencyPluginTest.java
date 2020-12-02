package com.heirteir.hac.util.dependency.plugin;

import lombok.SneakyThrows;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DependencyPluginTest extends DependencyPlugin {
    public DependencyPluginTest() {
        super("HAC");
    }

    protected DependencyPluginTest(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
    }

    @Override
    protected void enable() {
        //not used
    }

    @SneakyThrows
    @Override
    protected void disable() {
        try (Stream<Path> files = Files.walk(this.getPluginFolder().resolve("log"))) {
            for (Path path : files
                    .sorted(Comparator.reverseOrder())
                    .collect(Collectors.toList())) {

                Files.delete(path);
            }
        }

        Files.deleteIfExists(this.getPluginFolder().resolve("log"));
    }

    @Override
    protected void load() {
        //not used
    }
}
