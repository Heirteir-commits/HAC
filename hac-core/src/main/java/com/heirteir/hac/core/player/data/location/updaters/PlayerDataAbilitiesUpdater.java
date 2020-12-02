package com.heirteir.hac.core.player.data.location.updaters;

import com.heirteir.hac.api.events.types.Priority;
import com.heirteir.hac.api.events.types.packets.AbstractPacketEventExecutor;
import com.heirteir.hac.api.events.types.packets.wrapper.in.AbilitiesPacket;
import com.heirteir.hac.api.player.HACPlayer;
import com.heirteir.hac.core.player.data.location.PlayerData;
import org.apache.commons.lang.NotImplementedException;
import org.jetbrains.annotations.NotNull;

public final class PlayerDataAbilitiesUpdater extends AbstractPacketEventExecutor<AbilitiesPacket> {
    public PlayerDataAbilitiesUpdater() {
        super(AbilitiesPacket.class, Priority.PROCESS_1);
    }

    @Override
    protected boolean update(HACPlayer player, @NotNull AbilitiesPacket packet) {
        player.getDataManager().getData(PlayerData.class).setFlying(packet.isFlying());
        return true;
    }

    @Override
    protected void onStop(HACPlayer player, @NotNull AbilitiesPacket packet) {
        throw new NotImplementedException();
    }
}
