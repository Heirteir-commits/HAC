package com.heretere.hac.movement.simulator;

import org.jetbrains.annotations.NotNull;

public interface Simulator {
    @NotNull SimulationPoint processTick(@NotNull SimulationPoint input);
}
