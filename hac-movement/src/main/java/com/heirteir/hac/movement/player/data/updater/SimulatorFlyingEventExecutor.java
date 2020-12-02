package com.heirteir.hac.movement.player.data.updater;

import com.heirteir.hac.api.events.types.Priority;
import com.heirteir.hac.api.events.types.packets.AbstractPacketEventExecutor;
import com.heirteir.hac.api.events.types.packets.wrapper.in.FlyingPacket;
import com.heirteir.hac.api.player.HACPlayer;
import com.heirteir.hac.movement.player.data.Simulator;
import org.apache.commons.lang.NotImplementedException;
import org.jetbrains.annotations.NotNull;

public final class SimulatorFlyingEventExecutor extends AbstractPacketEventExecutor<FlyingPacket> {

    public SimulatorFlyingEventExecutor() {
        super(FlyingPacket.class, Priority.PROCESS_2);
    }

    @Override
    protected boolean update(HACPlayer player, @NotNull FlyingPacket packet) {
        player.getDataManager().getData(Simulator.class).update();
        return true;
    }

    @Override
    protected void onStop(HACPlayer player, @NotNull FlyingPacket packet) {
        throw new NotImplementedException();
    }
}
