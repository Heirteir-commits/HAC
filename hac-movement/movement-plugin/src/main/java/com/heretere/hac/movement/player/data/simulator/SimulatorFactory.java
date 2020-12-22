package com.heretere.hac.movement.player.data.simulator;

import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.api.player.builder.AbstractDataFactory;
import com.heretere.hac.movement.Movement;
import com.heretere.hac.movement.player.data.simulator.executors.SimulatorFlyingExecutor;
import org.jetbrains.annotations.NotNull;

/**
 * The type Simulator factory.
 */
public final class SimulatorFactory extends AbstractDataFactory<Simulator> {
    /**
     * The movement proxy reference.
     */
    private final Movement movement;

    /**
     * Instantiates a new Simulator factory.
     *
     * @param api      the api
     * @param movement the movement plugin
     */
    public SimulatorFactory(
        @NotNull final HACAPI api,
        @NotNull final Movement movement
    ) {
        super(api, new SimulatorFlyingExecutor("simulator"));
        this.movement = movement;
    }

    @Override
    public Simulator build(@NotNull final HACPlayer player) {
        return new Simulator(this.movement.getProxy(), player);
    }
}
