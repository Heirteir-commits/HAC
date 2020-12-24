package com.heretere.hac.movement.player.data.simulator;

import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.api.player.factory.DataFactory;
import com.heretere.hac.movement.Movement;
import com.heretere.hac.movement.player.data.simulator.executors.SimulatorFlyingExecutor;
import org.jetbrains.annotations.NotNull;

/**
 * The type Simulator factory.
 */
public final class SimulatorFactory extends DataFactory<Simulator> {
    /**
     * The movement proxy reference.
     */
    private final @NotNull Movement movement;

    /**
     * Instantiates a new Simulator factory.
     *
     * @param api      the api
     * @param movement the movement plugin
     */
    public SimulatorFactory(
        final @NotNull HACAPI api,
        final @NotNull Movement movement
    ) {
        super(api, new SimulatorFlyingExecutor("simulator"));
        this.movement = movement;
    }

    @Override
    public @NotNull Simulator build(final @NotNull HACPlayer player) {
        return new Simulator(this.movement.getProxy(), player);
    }
}
