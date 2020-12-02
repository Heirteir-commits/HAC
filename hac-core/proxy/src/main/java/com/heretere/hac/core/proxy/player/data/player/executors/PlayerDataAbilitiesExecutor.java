package com.heretere.hac.core.proxy.player.data.player.executors;

import com.heretere.hac.api.events.types.Priority;
import com.heretere.hac.api.events.types.packets.AbstractPacketEventExecutor;
import com.heretere.hac.api.events.types.packets.wrapper.clientside.AbilitiesPacket;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.core.proxy.player.data.player.PlayerData;
import org.apache.commons.lang.NotImplementedException;
import org.jetbrains.annotations.NotNull;

public final class PlayerDataAbilitiesExecutor extends AbstractPacketEventExecutor<AbilitiesPacket> {


    public PlayerDataAbilitiesExecutor() {
        super(AbilitiesPacket.class, Priority.PROCESS_1);
    }

    @Override
    protected boolean update(HACPlayer player, @NotNull AbilitiesPacket packet) {
        player.getDataManager().getData(PlayerData.class).getCurrent().setFlying(packet.isFlying());
        return true;
    }

    @Override
    protected void onStop(HACPlayer player, @NotNull AbilitiesPacket packet) {
        throw new NotImplementedException();
    }
}
