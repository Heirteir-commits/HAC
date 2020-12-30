package com.heretere.hac.movement;

import com.heretere.hac.movement.simulator.SimulatorFactory;
import com.heretere.hac.util.proxy.VersionProxy;

public abstract class MovementVersionProxy implements VersionProxy {
    @Override public final void preLoad() {
        this.load();
    }

    @Override public final void preUnload() {
        this.unload();
    }

    public abstract SimulatorFactory getSimulatorFactory();
}
