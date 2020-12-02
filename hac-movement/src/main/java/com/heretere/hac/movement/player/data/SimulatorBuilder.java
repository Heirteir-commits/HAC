package com.heretere.hac.movement.player.data;

import com.heretere.hac.api.API;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.api.player.builder.AbstractDataBuilder;
import com.heretere.hac.movement.Movement;
import com.heretere.hac.movement.dynamic.entity.human.HACHumanCreator;
import com.heretere.hac.movement.player.data.updater.SimulatorEntityVelocityEventExecutor;
import com.heretere.hac.movement.player.data.updater.SimulatorFireworkPropelEventExecutor;
import com.heretere.hac.movement.player.data.updater.SimulatorFlyingEventExecutor;

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
