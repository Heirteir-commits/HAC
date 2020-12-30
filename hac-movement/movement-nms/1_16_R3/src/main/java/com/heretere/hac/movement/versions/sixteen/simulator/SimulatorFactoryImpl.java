package com.heretere.hac.movement.versions.sixteen.simulator;

import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.movement.simulator.Simulator;
import com.heretere.hac.movement.simulator.SimulatorFactory;
import org.jetbrains.annotations.NotNull;

public final class SimulatorFactoryImpl implements SimulatorFactory {
    @Override public @NotNull Simulator createSimulator(final @NotNull HACPlayer player) {
        return new SimulatorImpl(player);
    }
}
