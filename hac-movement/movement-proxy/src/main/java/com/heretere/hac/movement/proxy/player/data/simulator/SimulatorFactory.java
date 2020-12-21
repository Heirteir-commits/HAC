package com.heretere.hac.movement.proxy.player.data.simulator;

import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.api.player.builder.AbstractDataFactory;
import com.heretere.hac.movement.proxy.AbstractMovementVersionProxy;
import com.heretere.hac.movement.proxy.player.data.simulator.executors.SimulatorFlyingExecutor;
import org.jetbrains.annotations.NotNull;

/**
 * The type Simulator factory.
 */
public final class SimulatorFactory extends AbstractDataFactory<Simulator> {
    /**
     * The movement proxy reference.
     */
    private final AbstractMovementVersionProxy proxy;

    /**
     * Instantiates a new Simulator factory.
     *
     * @param api   the api
     * @param proxy the proxy
     */
    public SimulatorFactory(@NotNull final HACAPI api, @NotNull final AbstractMovementVersionProxy proxy) {
        super(api, new SimulatorFlyingExecutor("simulator"));
        this.proxy = proxy;
    }

    @Override
    public Simulator build(@NotNull final HACPlayer player) {
        return new Simulator(this.proxy, player);
    }
}
