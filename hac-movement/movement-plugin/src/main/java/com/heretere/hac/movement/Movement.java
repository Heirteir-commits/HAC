package com.heretere.hac.movement;

import com.heretere.hac.movement.proxy.AbstractMovementVersionProxy;
import com.heretere.hac.util.proxy.AbstractProxyPlugin;
import org.bukkit.plugin.java.annotation.dependency.Dependency;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.LogPrefix;
import org.bukkit.plugin.java.annotation.plugin.Plugin;

/**
 * The type Movement.
 */
/* Plugin.yml */
@Plugin(name = "HAC-Movement", version = "0.0.1")
@LogPrefix("-")
@ApiVersion(ApiVersion.Target.v1_13)
@Dependency("HAC-Core")

public final class Movement extends AbstractProxyPlugin<AbstractMovementVersionProxy> {
    /**
     * The entry point for the HAC movement module.
     */
    public Movement() {
        super(
                "HAC",
                "Movement",
                "com.heretere.hac.movement.proxy.versions",
                AbstractMovementVersionProxy.class
        );
    }

    @Override
    public void proxyLoad() {
        //not used
    }

    @Override
    public void proxyEnable() {
        super.getProxy().baseLoad();
    }

    @Override
    public void proxyDisable() {
        super.getProxy().baseUnload();
    }
}
