package com.heretere.hac.movement.simulator;

import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.events.PacketEventExecutor;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.api.player.factory.DataFactory;
import com.heretere.hac.movement.Movement;
import com.heretere.hac.movement.simulator.executors.SimulatorFlyingExecutor;
import org.jetbrains.annotations.NotNull;

public final class SimulatorDataFactory extends DataFactory<SimulatorData> {
    private static final String BASE_IDENTIFIER = "simulator";

    private final @NotNull Movement movement;

    /**
     * You can include a vararg set of {@link PacketEventExecutor} these will be
     * registered for you from {@link DataFactory#registerUpdaters()}.
     *
     * @param api    The HACAPI reference
     * @param events The instances of {@link PacketEventExecutor}
     */
    public SimulatorDataFactory(
        final @NotNull HACAPI api,
        final @NotNull Movement movement
    ) {
        super(api, new SimulatorFlyingExecutor(BASE_IDENTIFIER));
        this.movement = movement;
    }

    @Override public @NotNull SimulatorData build(final @NotNull HACPlayer player) {
        return new SimulatorData(this.movement.getProxy(), player);
    }
}
