package com.heretere.hac.movement.simulator;

import com.heretere.hac.api.player.HACPlayer;
import org.jetbrains.annotations.NotNull;

public interface SimulatorFactory {
    @NotNull Simulator createSimulator(@NotNull HACPlayer player);
}
