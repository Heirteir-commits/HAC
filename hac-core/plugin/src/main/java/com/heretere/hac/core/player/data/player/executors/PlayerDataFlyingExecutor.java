package com.heretere.hac.core.player.data.player.executors;

import com.heretere.hac.api.events.types.Priority;
import com.heretere.hac.api.events.types.packets.AbstractPacketEventExecutor;
import com.heretere.hac.api.events.types.packets.wrapper.clientside.FlyingPacket;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.core.player.data.player.PlayerData;
import org.apache.commons.lang.NotImplementedException;
import org.jetbrains.annotations.NotNull;

public class PlayerDataFlyingExecutor extends AbstractPacketEventExecutor<FlyingPacket> {
    public PlayerDataFlyingExecutor() {
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
