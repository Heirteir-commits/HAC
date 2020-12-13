package com.heretere.hac.api.config;

import com.google.common.collect.Maps;
import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.config.annotations.ConfigFile;
import com.heretere.hac.api.config.annotations.ConfigKey;
import com.heretere.hac.api.config.annotations.Section;
import com.heretere.hac.api.config.file.ConfigField;
import com.heretere.hac.api.config.file.ConfigPath;
import com.heretere.hac.api.config.file.ConfigSection;
import com.heretere.hac.api.config.file.HACConfigFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

public class HACConfigHandler {
    private final Path basePath;

    private final Map<String, HACConfigFile> files;

    public HACConfigHandler() {
        this.basePath = JavaPlugin.getProvidingPlugin(HACConfigHandler.class).getDataFolder().toPath().getParent().resolve("HAC");

        this.files = Maps.newHashMap();
    }

    public void loadConfigClass(Object instance) {
        Class<?> clazz = instance.getClass();

        if (!clazz.isAnnotationPresent(ConfigFile.class)) {
            return;
        }

        HACConfigFile file = this.getConfigFile(clazz.getAnnotation(ConfigFile.class));

        Map<String, ConfigPath> configValues = Maps.newHashMap();

        if (clazz.isAnnotationPresent(Section.class)) {
            Section section = clazz.getAnnotation(Section.class);
            configValues.computeIfAbsent(section.key(), path -> new ConfigSection(path, section.comments()));
        }

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(ConfigKey.class)) {
                ConfigKey configKey = field.getAnnotation(ConfigKey.class);

                ConfigField<?> configField = (ConfigField<?>)
                        configValues.computeIfAbsent(configKey.path(), path -> new ConfigField<>(field.getType(), instance, path, configKey.comments()));

                Optional<Method> setter = Arrays.stream(clazz.getMethods())
                        .filter(method -> method.getName().equals(configKey.setter()))
                        .findFirst();

                if (setter.isPresent()) {
                    configField.setSetter(setter.get());
                } else {
                    HACAPI.getInstance().getErrorHandler().getHandler().accept(
                            new NoSuchMethodException(String.format(
                                    "Setter with name '%s' does not exist in class '%s'.",
                                    configKey.setter(),
                                    clazz.getName()
                            ))
                    );
                }

                Optional<Method> getter = Arrays.stream(clazz.getMethods())
                        .filter(method -> method.getName().equals(configKey.getter()))
                        .findFirst();

                if (getter.isPresent()) {
                    configField.setGetter(getter.get());
                } else {
                    HACAPI.getInstance().getErrorHandler().getHandler().accept(
                            new NoSuchMethodException(String.format(
                                    "Getter with name '%s' does not exist in class '%s'.",
                                    configKey.getter(),
                                    clazz.getName()
                            ))
                    );
                }
            }
        }

        configValues.values().forEach(file::loadConfigPath);
    }

    public void unload() {
        this.files.values().forEach(HACConfigFile::save);
    }

    private HACConfigFile getConfigFile(ConfigFile path) {
        return files.computeIfAbsent(path.value(), v -> new HACConfigFile(this, path));
    }

    public Path getBasePath() {
        return basePath;
    }
}
