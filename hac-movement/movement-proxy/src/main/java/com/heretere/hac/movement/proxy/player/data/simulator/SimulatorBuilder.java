package com.heretere.hac.movement.proxy.player.data.simulator;

import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.api.player.builder.AbstractDataBuilder;
import com.heretere.hac.movement.proxy.AbstractMovementVersionProxy;
import com.heretere.hac.movement.proxy.player.data.simulator.executors.SimulatorFlyingExecutor;
import org.jetbrains.annotations.NotNull;

public class SimulatorBuilder extends AbstractDataBuilder<Simulator> {
    private final AbstractMovementVersionProxy proxy;

    public SimulatorBuilder(@NotNull HACAPI api, @NotNull AbstractMovementVersionProxy proxy) {
        super(api, new SimulatorFlyingExecutor("simulator"));
        this.proxy = proxy;
    }

    @Override
    public Simulator build(@NotNull HACPlayer player) {
        return new Simulator(this.proxy, player);
    }
}
