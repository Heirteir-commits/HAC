package com.heretere.hac.movement.simulator.executors;

import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.events.PacketEventExecutor;
import com.heretere.hac.api.events.Priority;
import com.heretere.hac.api.events.packets.wrapper.clientside.FlyingPacket;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.movement.simulator.SimulatorData;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;

public final class SimulatorFlyingExecutor extends PacketEventExecutor<FlyingPacket> {
    /**
     * @param api        The HACAPI reference
     * @param identifier the identifier
     */
    public SimulatorFlyingExecutor(
        final @NotNull HACAPI api,
        final @NotNull String identifier
    ) {
        super(
            Priority.PROCESS_2,
            identifier,
            api.getPacketReferences().getClientSide().getFlying(),
            true
        );
    }

    @Override protected boolean execute(
        final @NotNull HACPlayer player,
        final @NotNull FlyingPacket packet
    ) {
        player.getDataManager().getData(SimulatorData.class).ifPresent(SimulatorData::update);
        return true;
    }

    @Override protected void onStop(
        final @NotNull HACPlayer player,
        final @NotNull FlyingPacket packet
    ) {
        throw new NotImplementedException("Updater class.");
    }
}
