package com.heretere.hac.core.proxy.player.executors;

import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.events.PacketEventExecutor;
import com.heretere.hac.api.events.Priority;
import com.heretere.hac.api.events.packets.wrapper.clientside.FlyingPacket;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.core.proxy.player.PlayerData;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;

/**
 * The type Player data flying executor.
 */
public final class PlayerDataFlyingExecutor extends PacketEventExecutor<FlyingPacket> {
    /**
     * Instantiates a new Player data flying executor.
     *
     * @param api        the HACAPI reference
     * @param identifier the identifier
     */
    public PlayerDataFlyingExecutor(
        final @NotNull HACAPI api,
        final @NotNull String identifier
    ) {
        super(
            Priority.PROCESS_1,
            identifier,
            api.getPacketReferences().getClientSide().getFlying(),
            false
        );
    }

    @Override
    public boolean execute(
        final @NotNull HACPlayer player,
        final @NotNull FlyingPacket packet
    ) {
        player.getDataManager().getData(PlayerData.class).ifPresent(playerData -> playerData.update(packet));

        return true;
    }

    @Override
    public void onStop(
        final @NotNull HACPlayer player,
        final @NotNull FlyingPacket packet
    ) {
        throw new NotImplementedException("Updater Class.");
    }
}
