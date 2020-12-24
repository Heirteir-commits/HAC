package com.heretere.hac.movement.player.data.simulator.executors;

import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.events.PacketEventExecutor;
import com.heretere.hac.api.events.Priority;
import com.heretere.hac.api.events.packets.wrapper.clientside.FlyingPacket;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.movement.player.data.simulator.Simulator;
import org.apache.commons.lang.NotImplementedException;
import org.jetbrains.annotations.NotNull;

/**
 * The type Simulator flying executor.
 */
public final class SimulatorFlyingExecutor extends PacketEventExecutor<FlyingPacket> {
    /**
     * Instantiates a new Simulator flying executor.
     *
     * @param identifier the identifier
     */
    public SimulatorFlyingExecutor(final @NotNull String identifier) {
        super(Priority.PROCESS_2, identifier, HACAPI.getInstance().getPacketReferences().getClientSide().getFlying());
    }

    @Override
    protected boolean execute(
        final @NotNull HACPlayer player,
        final @NotNull FlyingPacket packet
    ) {
        player.getDataManager().getData(Simulator.class).ifPresent(Simulator::update);
        return true;
    }

    @Override
    protected void onStop(
        final @NotNull HACPlayer player,
        final @NotNull FlyingPacket packet
    ) {
        throw new NotImplementedException();
    }
}
