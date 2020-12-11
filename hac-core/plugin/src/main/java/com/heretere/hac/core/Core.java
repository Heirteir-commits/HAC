package com.heretere.hac.core;

import com.heretere.hac.util.plugin.AbstractHACPlugin;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.LogPrefix;
import org.bukkit.plugin.java.annotation.plugin.Plugin;

@Plugin(name = "HAC-Core", version = "0.0.1")
@LogPrefix("-")
@ApiVersion(ApiVersion.Target.v1_13)
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
