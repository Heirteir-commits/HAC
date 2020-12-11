package com.heretere.hac.core;

import com.heretere.hac.util.plugin.AbstractHACPlugin;
import com.heretere.hac.util.plugin.dependency.annotations.Maven;
import com.heretere.hac.util.plugin.dependency.relocation.annotations.Relocation;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.LogPrefix;
import org.bukkit.plugin.java.annotation.plugin.Plugin;

@Plugin(name = "HAC-Core", version = "0.0.1")
@LogPrefix("-")
@ApiVersion(ApiVersion.Target.v1_13)
@Maven(groupId = "com.google.guava", artifactId = "guava", version = "30.0-jre")
@Relocation(from = "com.google.common", to = "com.heretere.hac.libs.guava")
public final class Core extends AbstractHACPlugin {
    public Core() {
        super("HAC", "Core");
    }

    @Override
    public void load() {

    }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {

    }

}
