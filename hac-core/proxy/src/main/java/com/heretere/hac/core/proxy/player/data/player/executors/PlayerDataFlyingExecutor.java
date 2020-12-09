package com.heretere.hac.core.proxy.player.data.player.executors;

import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.events.AbstractPacketEventExecutor;
import com.heretere.hac.api.events.Priority;
import com.heretere.hac.api.events.packets.wrapper.clientside.FlyingPacket;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.core.proxy.player.data.player.PlayerData;
import org.apache.commons.lang.NotImplementedException;
import org.jetbrains.annotations.NotNull;

public final class PlayerDataFlyingExecutor extends AbstractPacketEventExecutor<FlyingPacket> {
    public PlayerDataFlyingExecutor(String identifier) {
        super(
                Priority.PROCESS_1,
                identifier,
                HACAPI.getInstance().getPacketReferences().getClientSide().getFlying()
        );
    }

    @Override
    public boolean execute(@NotNull HACPlayer player, @NotNull FlyingPacket packet) {
        player.getDataManager().getData(PlayerData.class).update(packet);
        return true;
    }

    @Override
    public void onStop(@NotNull HACPlayer player, @NotNull FlyingPacket packet) {
        throw new NotImplementedException("Updater Class.");
    }
}
