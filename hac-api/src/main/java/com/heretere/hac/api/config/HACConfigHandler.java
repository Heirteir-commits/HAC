package com.heretere.hac.api.config;

import com.google.common.collect.Maps;
import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.config.annotations.ConfigFile;
import com.heretere.hac.api.config.annotations.ConfigKey;
import com.heretere.hac.api.config.annotations.ConfigSection;
import com.heretere.hac.api.config.file.ConfigField;
import com.heretere.hac.api.config.file.ConfigPath;
import com.heretere.hac.api.config.file.HACConfigFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

/**
 * The type Hac config handler.
 */
public class HACConfigHandler {
    /**
     * The HAC API reference.
     */
    private final HACAPI api;
    /**
     * The base path of the config files.
     */
    private final Path basePath;

    /**
     * Only currently loaded files.
     */
    private final Map<String, HACConfigFile> files;

    /**
     * Instantiates a new Hac config handler.
     *
     * @param api the api
     */
    public HACConfigHandler(@NotNull final HACAPI api) {
        this.api = api;
        this.basePath = JavaPlugin.getProvidingPlugin(HACConfigHandler.class)
                                  .getDataFolder()
                                  .toPath()
                                  .getParent()
                                  .resolve("HAC");

        this.files = Maps.newHashMap();
    }

    /**
     * Load config class.
     *
     * @param instance the instance
     */
    public void loadConfigClass(@NotNull final Object instance) {
        Class<?> clazz = instance.getClass();

        if (!clazz.isAnnotationPresent(ConfigFile.class)) {
            return;
        }

        HACConfigFile file = this.getConfigFile(clazz.getAnnotation(ConfigFile.class));

        Map<String, ConfigPath> configValues = Maps.newHashMap();

        if (clazz.isAnnotationPresent(ConfigSection.class)) {
            ConfigSection configSection = clazz.getAnnotation(ConfigSection.class);
            configValues.computeIfAbsent(
                    configSection.key(),
                    path -> new com.heretere.hac.api.config.file.ConfigSection(
                            this.api,
                            path,
                            configSection.comments()
                    )
            );
        }

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(ConfigKey.class)) {
                ConfigKey configKey = field.getAnnotation(ConfigKey.class);

                ConfigField<?> configField = (ConfigField<?>) configValues.computeIfAbsent(
                        configKey.path(),
                        path -> new ConfigField<>(
                                this.api,
                                field.getType(),
                                instance,
                                path,
                                configKey.comments()
                        )
                );

                Optional<Method> setter = Arrays.stream(clazz.getMethods())
                                                .filter(method -> method.getName().equals(configKey.setter()))
                                                .findFirst();

                if (setter.isPresent()) {
                    configField.setSetter(setter.get());
                } else {
                    this.api.getErrorHandler().getHandler().accept(new NoSuchMethodException(String.format(
                            "Setter with name '%s' does not exist in class '%s'.",
                            configKey.setter(),
                            clazz.getName()
                    )));
                }

                Optional<Method> getter = Arrays.stream(clazz.getMethods())
                                                .filter(method -> method.getName().equals(configKey.getter()))
                                                .findFirst();

                if (getter.isPresent()) {
                    configField.setGetter(getter.get());
                } else {
                    this.api.getErrorHandler().getHandler().accept(new NoSuchMethodException(String.format(
                            "Getter with name '%s' does not exist in class '%s'.",
                            configKey.getter(),
                            clazz.getName()
                    )));
                }
            }
        }

        configValues.values().forEach(file::loadConfigPath);
    }

    /**
     * Saves all the loaded config files.
     */
    public void unload() {
        this.files.values().forEach(HACConfigFile::save);
    }

    private HACConfigFile getConfigFile(@NotNull final ConfigFile path) {
        return files.computeIfAbsent(path.value(), v -> new HACConfigFile(this.api, this, path));
    }

    /**
     * Gets base path.
     *
     * @return the base path
     */
    public Path getBasePath() {
        return basePath;
    }
}
