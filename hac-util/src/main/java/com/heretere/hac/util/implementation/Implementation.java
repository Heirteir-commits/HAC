package com.heretere.hac.util.implementation;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Set;

public abstract class Implementation {
    private final Map<Class<?>, ?> implementations;
    private final String basePackage;

    public Implementation(String basePackage) {
        this.implementations = Maps.newHashMap();
        this.basePackage = basePackage;
    }

    public void load() {
        Set<String> availableVersions = VersionProcessor.getAvailableVersions(this.basePackage);
        String version = VersionProcessor.getServerVersionString();

    }

    public void unload() {

    }
}
