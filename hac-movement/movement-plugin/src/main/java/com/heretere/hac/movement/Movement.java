package com.heretere.hac.movement;

import com.heretere.hac.api.HACAPI;
import com.heretere.hac.movement.simulator.SimulatorData;
import com.heretere.hac.movement.simulator.SimulatorDataFactory;
import com.heretere.hac.util.proxy.ProxyPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.annotation.dependency.Dependency;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.LogPrefix;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/* Plugin.yml */
@Plugin(name = "HAC-Movement", version = "0.0.1")
@LogPrefix("-")
@ApiVersion(ApiVersion.Target.v1_13)
@Dependency("HAC-Core")

public final class Movement extends ProxyPlugin<MovementVersionProxy> {
    /**
     * The HACAPI reference.
     */
    private @Nullable HACAPI api;

    private @Nullable SimulatorDataFactory simulatorDataFactory;

    public Movement() {
        super(
            "HAC",
            "Movement",
            "com.heretere.hac.movement.versions",
            MovementVersionProxy.class
        );
    }

    @Override public void proxyLoad() {
        this.api = Objects.requireNonNull(Bukkit.getServicesManager().load(HACAPI.class));
        this.simulatorDataFactory = new SimulatorDataFactory(this.api, this);
    }

    @Override public void proxyEnable() {
        if (this.api == null || this.simulatorDataFactory == null) {
            super.getLog()
                 .reportFatalError(
                     () -> "HAC didn't start correctly please check the latest.log for more info.",
                     true
                 );
            return;
        }

        this.api
            .getHacPlayerList()
            .getFactory()
            .registerDataBuilder(SimulatorData.class, this.simulatorDataFactory);

        super.getProxy().preLoad();
    }

    @Override public void proxyDisable() {
        if (this.api == null || this.simulatorDataFactory == null) {
            return;
        }

        super.getProxy().preUnload();
        this.api.getHacPlayerList().getFactory().unregisterDataBuilder(SimulatorData.class);
    }
}
