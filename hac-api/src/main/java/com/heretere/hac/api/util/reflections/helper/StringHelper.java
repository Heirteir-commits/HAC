package com.heretere.hac.api.util.reflections.helper;

import com.google.common.collect.ImmutableSet;
import com.heretere.hac.api.API;
import com.heretere.hac.api.util.reflections.Reflections;
import org.apache.commons.lang.StringUtils;

import java.util.AbstractMap;

public final class StringHelper {
    private final ImmutableSet<AbstractMap.SimpleImmutableEntry<String, String>> replacements;

    public StringHelper(Reflections reflections) {
        this.replacements = ImmutableSet.of(
                new AbstractMap.SimpleImmutableEntry<>("%api%", API.class.getPackage().getName()),
                new AbstractMap.SimpleImmutableEntry<>("%nms%", "net.minecraft.server.v" + reflections.getVersion().getPackageName()),
                new AbstractMap.SimpleImmutableEntry<>("%cb%", "org.bukkit.craftbukkit.v" + reflections.getVersion().getPackageName())
        );
    }

    public String replaceString(String input) {
        String output = input;

        for (AbstractMap.SimpleImmutableEntry<String, String> entry : replacements) {
            output = StringUtils.replace(output, entry.getKey(), entry.getValue());
        }

        return output;
    }
}
