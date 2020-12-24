package com.heretere.hac.core.proxy.player.executors;

import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.events.PacketEventExecutor;
import com.heretere.hac.api.events.Priority;
import com.heretere.hac.api.events.packets.wrapper.clientside.AbilitiesPacket;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.core.proxy.player.PlayerData;
import org.apache.commons.lang.NotImplementedException;
import org.jetbrains.annotations.NotNull;

public final class PlayerDataAbilitiesExecutor extends PacketEventExecutor<AbilitiesPacket> {
    /**
     * Instantiates a new Player data abilities executor.
     *
     * @param identifier the identifier
     */
    public PlayerDataAbilitiesExecutor(final @NotNull String identifier) {
        super(
            Priority.PROCESS_1,
            identifier,
            HACAPI.getInstance().getPacketReferences().getClientSide().getAbilities()
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
