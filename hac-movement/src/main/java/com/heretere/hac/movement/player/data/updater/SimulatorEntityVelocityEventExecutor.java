package com.heretere.hac.movement.player.data.updater;

import com.flowpowered.math.vector.Vector3f;
import com.heretere.hac.api.events.types.Priority;
import com.heretere.hac.api.events.types.packets.AbstractPacketEventExecutor;
import com.heretere.hac.api.events.types.packets.wrapper.serverside.EntityVelocityPacket;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.movement.player.data.Simulator;
import org.apache.commons.lang.NotImplementedException;
import org.jetbrains.annotations.NotNull;

public class SimulatorEntityVelocityEventExecutor extends AbstractPacketEventExecutor<EntityVelocityPacket> {
    public SimulatorEntityVelocityEventExecutor() {
        super(EntityVelocityPacket.class, Priority.PROCESS_1);
    }

    @Override
    protected boolean update(HACPlayer player, @NotNull EntityVelocityPacket packet) {
        player.getDataManager().getData(Simulator.class).getClosestMatch()
                .setMotion(new Vector3f(packet.getX(), packet.getY(), packet.getZ()));

        return true;
    }

    @Override
    protected void onStop(HACPlayer player, @NotNull EntityVelocityPacket packet) {
        throw new NotImplementedException();
    }
}
