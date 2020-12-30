package com.heretere.hac.movement.versions.sixteen;

import com.heretere.hac.movement.MovementVersionProxy;
import com.heretere.hac.movement.simulator.SimulatorFactory;
import com.heretere.hac.movement.versions.sixteen.simulator.SimulatorFactoryImpl;
import org.jetbrains.annotations.NotNull;

public class Proxy extends MovementVersionProxy {
    private final @NotNull SimulatorFactoryImpl simulatorFactory;

    public Proxy() {
        this.simulatorFactory = new SimulatorFactoryImpl();
    }

    @Override public void load() {
        //not used
    }

    @Override public void unload() {
        //not used
    }

    @Override public SimulatorFactory getSimulatorFactory() {
        return this.simulatorFactory;
    }
}
