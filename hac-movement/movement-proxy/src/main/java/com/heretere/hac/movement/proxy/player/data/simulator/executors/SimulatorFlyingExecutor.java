package com.heretere.hac.movement.proxy.player.data.simulator.executors;

import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.events.AbstractPacketEventExecutor;
import com.heretere.hac.api.events.Priority;
import com.heretere.hac.api.events.packets.wrapper.clientside.FlyingPacket;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.movement.proxy.player.data.simulator.Simulator;
import org.apache.commons.lang.NotImplementedException;
import org.jetbrains.annotations.NotNull;

public class SimulatorFlyingExecutor extends AbstractPacketEventExecutor<FlyingPacket> {
    public SimulatorFlyingExecutor(@NotNull String identifier) {
        super(Priority.PROCESS_2, identifier, HACAPI.getInstance().getPacketReferences().getClientSide().getFlying());
    }

    @Override
    protected boolean execute(@NotNull HACPlayer player, @NotNull FlyingPacket packet) {
        player.getDataManager().getData(Simulator.class).update();
        return true;
    }

    @Override
    protected void onStop(@NotNull HACPlayer player, @NotNull FlyingPacket packet) {
        throw new NotImplementedException();
    }
}
