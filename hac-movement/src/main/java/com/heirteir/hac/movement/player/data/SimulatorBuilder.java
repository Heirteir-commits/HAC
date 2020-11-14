package com.heirteir.hac.movement.player.data;

import com.heirteir.hac.api.API;
import com.heirteir.hac.api.player.HACPlayer;
import com.heirteir.hac.api.player.builder.AbstractDataBuilder;
import com.heirteir.hac.movement.Movement;
import com.heirteir.hac.movement.dynamic.entity.human.HACHumanCreator;
import com.heirteir.hac.movement.player.data.updater.SimulatorEntityVelocityEventExecutor;
import com.heirteir.hac.movement.player.data.updater.SimulatorFireworkPropelEventExecutor;
import com.heirteir.hac.movement.player.data.updater.SimulatorFlyingEventExecutor;

public class SimulatorBuilder extends AbstractDataBuilder<Simulator> {
    private final SimulatorFireworkPropelEventExecutor fireworkPropelEventExecutor;

    private final Movement movement;

    public SimulatorBuilder(Movement movement) {
        super(new SimulatorFlyingEventExecutor(), new SimulatorEntityVelocityEventExecutor());
        this.fireworkPropelEventExecutor = new SimulatorFireworkPropelEventExecutor();
        this.movement = movement;
    }

    @Override
    public Simulator build(HACPlayer player) {
        return new Simulator(this.movement, player, movement.getClassManager().getDynamicClass(HACHumanCreator.class).getHumanConstructor(),
                movement.getClassManager().getDynamicClass(HACHumanCreator.class).getFoodMetaDataConstructor());
    }

    @Override
    public void registerUpdaters() {
        super.registerUpdaters();
        API.INSTANCE.getEventManager().addEvent(this.fireworkPropelEventExecutor);
    }

    @Override
    public void removeUpdaters() {
        super.removeUpdaters();
        API.INSTANCE.getEventManager().removeEvent(this.fireworkPropelEventExecutor);
    }
}
