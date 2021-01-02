package com.heretere.hac.core.proxy.player.executors;

import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.event.PacketEventExecutor;
import com.heretere.hac.api.event.Priority;
import com.heretere.hac.api.event.packet.wrapper.clientside.AbilitiesPacket;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.core.proxy.player.PlayerData;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;

public final class PlayerDataAbilitiesExecutor extends PacketEventExecutor<AbilitiesPacket> {
    /**
     * Instantiates a new Player data abilities executor.
     *
     * @param api        the HACAPI reference
     * @param identifier the identifier
     */
    public PlayerDataAbilitiesExecutor(
        final @NotNull HACAPI api,
        final @NotNull String identifier
    ) {
        super(
            Priority.PROCESS_1,
            identifier,
            api.getPacketReferences().getClientSide().getAbilities(),
            false
        );
    }

    @Override
    public boolean execute(
        final @NotNull HACPlayer player,
        final @NotNull AbilitiesPacket packet
    ) {
        player.getDataManager()
              .getData(PlayerData.class)
              .ifPresent(playerData -> playerData.getCurrent().setFlying(true));

        return true;
    }

    @Override
    public void onStop(
        final @NotNull HACPlayer player,
        final @NotNull AbilitiesPacket packet
    ) {
        throw new NotImplementedException("Updater Class");
    }
}
