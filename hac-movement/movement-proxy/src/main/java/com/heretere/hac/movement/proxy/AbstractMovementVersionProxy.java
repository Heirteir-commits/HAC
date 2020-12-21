package com.heretere.hac.movement.proxy;

import com.heretere.hac.api.HACAPI;
import com.heretere.hac.movement.proxy.player.data.simulator.Simulator;
import com.heretere.hac.movement.proxy.player.data.simulator.SimulatorFactory;
import com.heretere.hac.util.proxy.AbstractVersionProxy;

public abstract class AbstractMovementVersionProxy implements AbstractVersionProxy {
    /**
     * The factory responsible for attaching a simulator to each HACPlayer.
     */
    private final SimulatorFactory simulatorFactory;

    protected AbstractMovementVersionProxy() {
        this.simulatorFactory = new SimulatorFactory(HACAPI.getInstance(), this);
    }

    /**
     * Loads proxy plugin logic used by all version proxies, then delegates the rest of the loading to the version
     * proxy.
     */
    public final void baseLoad() {
        HACAPI.getInstance().getHacPlayerList().getBuilder()
                .registerDataBuilder(Simulator.class, this.simulatorFactory);
        this.load();
    }

    /**
     * Unloads proxy plugin logic used by all version proxies, and delegates unloading to the version proxy.
     */
    public final void baseUnload() {
        this.unload();
        HACAPI.getInstance().getHacPlayerList().getBuilder().unregisterDataBuilder(Simulator.class);
    }

    protected abstract void load();

    protected abstract void unload();
}
