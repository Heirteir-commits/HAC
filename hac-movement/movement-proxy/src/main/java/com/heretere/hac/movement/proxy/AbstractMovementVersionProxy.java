package com.heretere.hac.movement.proxy;

import com.heretere.hac.api.HACAPI;
import com.heretere.hac.movement.proxy.player.data.simulator.Simulator;
import com.heretere.hac.movement.proxy.player.data.simulator.SimulatorBuilder;
import com.heretere.hac.util.proxy.AbstractVersionProxy;

public abstract class AbstractMovementVersionProxy implements AbstractVersionProxy {
    private final SimulatorBuilder simulatorBuilder;

    protected AbstractMovementVersionProxy() {
        this.simulatorBuilder = new SimulatorBuilder(HACAPI.getInstance(), this);
    }

    public final void baseLoad() {
        HACAPI.getInstance().getHacPlayerList().getBuilder().registerDataBuilder(Simulator.class, this.simulatorBuilder);
        this.load();
    }

    public final void baseUnload() {
        this.unload();
        HACAPI.getInstance().getHacPlayerList().getBuilder().unregisterDataBuilder(Simulator.class);
    }

    public abstract TestWorldHelper getWorldHelper();

    protected abstract void load();

    protected abstract void unload();
}
