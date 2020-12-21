package com.heretere.hac.core.proxy.player.executors;

import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.events.AbstractPacketEventExecutor;
import com.heretere.hac.api.events.Priority;
import com.heretere.hac.api.events.packets.wrapper.clientside.FlyingPacket;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.core.proxy.player.PlayerData;
import org.apache.commons.lang.NotImplementedException;
import org.jetbrains.annotations.NotNull;

/**
 * The type Player data flying executor.
 */
public final class PlayerDataFlyingExecutor extends AbstractPacketEventExecutor<FlyingPacket> {
    /**
     * Instantiates a new Player data flying executor.
     *
     * @param identifier the identifier
     */
    public PlayerDataFlyingExecutor(@NotNull final String identifier) {
        super(
                Priority.PROCESS_1,
                identifier,
                HACAPI.getInstance().getPacketReferences().getClientSide().getFlying()
        );
    }

    @Override
    public boolean execute(@NotNull final HACPlayer player, @NotNull final FlyingPacket packet) {
        player.getDataManager().getData(PlayerData.class).update(packet);
        return true;
    }

    @Override
    public void onStop(@NotNull final HACPlayer player, @NotNull final FlyingPacket packet) {
        throw new NotImplementedException("Updater Class.");
    }
}
