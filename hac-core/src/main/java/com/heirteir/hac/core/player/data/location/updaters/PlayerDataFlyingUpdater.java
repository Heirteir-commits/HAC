package com.heirteir.hac.core.player.data.location.updaters;

import com.heirteir.hac.api.events.types.Priority;
import com.heirteir.hac.api.events.types.packets.AbstractPacketEventExecutor;
import com.heirteir.hac.api.events.types.packets.wrapper.in.FlyingPacket;
import com.heirteir.hac.api.player.HACPlayer;
import com.heirteir.hac.core.player.data.location.PlayerData;
import org.apache.commons.lang.NotImplementedException;
import org.jetbrains.annotations.NotNull;

public final class PlayerDataFlyingUpdater extends AbstractPacketEventExecutor<FlyingPacket> {
    public PlayerDataFlyingUpdater() {
        super(FlyingPacket.class, Priority.PROCESS_1);
    }

    @Override
    protected boolean update(HACPlayer player, @NotNull FlyingPacket packet) {
        player.getDataManager().getData(PlayerData.class).update(packet);
        return true;
    }

    @Override
    protected void onStop(HACPlayer player, @NotNull FlyingPacket packet) {
        throw new NotImplementedException();
    }
}
