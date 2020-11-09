package com.heirteir.hac.movement.player.data;

import com.heirteir.hac.api.player.HACPlayer;
import com.heirteir.hac.api.player.builder.AbstractDataBuilder;
import com.heirteir.hac.movement.Movement;
import com.heirteir.hac.movement.dynamic.entity.human.HACHumanCreator;
import com.heirteir.hac.movement.player.data.updater.SimulatorFlyingUpdater;

public class SimulatorBuilder extends AbstractDataBuilder<Simulator> {
    private final Movement movement;

    public SimulatorBuilder(Movement movement) {
        super(new SimulatorFlyingUpdater());
        this.movement = movement;
    }

    @Override
    public Simulator build(HACPlayer player) {
        return new Simulator(this.movement, player, movement.getClassManager().getDynamicClass(HACHumanCreator.class).getDynamic());
    }
}
