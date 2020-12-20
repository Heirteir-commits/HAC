package com.heretere.hac.core.proxy.player.data.player.executors;

import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.events.AbstractPacketEventExecutor;
import com.heretere.hac.api.events.Priority;
import com.heretere.hac.api.events.packets.wrapper.clientside.AbilitiesPacket;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.core.proxy.player.data.player.PlayerData;
import org.apache.commons.lang.NotImplementedException;
import org.jetbrains.annotations.NotNull;

public final class PlayerDataAbilitiesExecutor extends AbstractPacketEventExecutor<AbilitiesPacket> {
    public PlayerDataAbilitiesExecutor(String identifier) {
        super(
                Priority.PROCESS_1,
                identifier,
                HACAPI.getInstance().getPacketReferences().getClientSide().getAbilities()
        );
    }

    @Override
    public boolean execute(@NotNull HACPlayer player, @NotNull AbilitiesPacket packet) {
        player.getDataManager().getData(PlayerData.class).getCurrent().setFlying(packet.isFlying());
        return true;
    }

    @Override
    public void onStop(@NotNull HACPlayer player, @NotNull AbilitiesPacket packet) {
        throw new NotImplementedException("Updater Class");
    }
}
