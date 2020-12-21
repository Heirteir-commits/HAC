package com.heretere.hac.movement.player.data.simulator.executors;

import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.events.AbstractPacketEventExecutor;
import com.heretere.hac.api.events.Priority;
import com.heretere.hac.api.events.packets.wrapper.clientside.FlyingPacket;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.movement.player.data.simulator.Simulator;
import org.apache.commons.lang.NotImplementedException;
import org.jetbrains.annotations.NotNull;

/**
 * The type Simulator flying executor.
 */
public final class SimulatorFlyingExecutor extends AbstractPacketEventExecutor<FlyingPacket> {
    /**
     * Instantiates a new Simulator flying executor.
     *
     * @param identifier the identifier
     */
    public SimulatorFlyingExecutor(@NotNull final String identifier) {
        super(Priority.PROCESS_2, identifier, HACAPI.getInstance().getPacketReferences().getClientSide().getFlying());
    }

    @Override
    protected boolean execute(@NotNull final HACPlayer player, @NotNull final FlyingPacket packet) {
        player.getDataManager().getData(Simulator.class).update();
        return true;
    }

    @Override
    protected void onStop(@NotNull final HACPlayer player, @NotNull final FlyingPacket packet) {
        throw new NotImplementedException();
    }
}
