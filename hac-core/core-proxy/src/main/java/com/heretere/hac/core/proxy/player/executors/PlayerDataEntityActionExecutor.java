package com.heretere.hac.core.proxy.player.executors;

import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.events.PacketEventExecutor;
import com.heretere.hac.api.events.Priority;
import com.heretere.hac.api.events.packets.wrapper.clientside.EntityActionPacket;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.core.proxy.player.PlayerData;
import org.apache.commons.lang.NotImplementedException;
import org.jetbrains.annotations.NotNull;

/**
 * The type Player data entity action executor.
 */
public final class PlayerDataEntityActionExecutor extends PacketEventExecutor<EntityActionPacket> {

    /**
     * Instantiates a new Player data entity action executor.
     *
     * @param identifier the identifier
     */
    public PlayerDataEntityActionExecutor(final @NotNull String identifier) {
        super(
            Priority.PROCESS_1,
            identifier,
            HACAPI.getInstance().getPacketReferences().getClientSide().getEntityAction(),
            false
        );
    }

    @Override
    public boolean execute(
        final @NotNull HACPlayer player,
        final @NotNull EntityActionPacket packet
    ) {
        player.getDataManager().getData(PlayerData.class).ifPresent(playerData -> {
            switch (packet.getAction()) {
                case START_SNEAKING:
                    playerData.getCurrent().setSneaking(true);
                    break;
                case STOP_SNEAKING:
                    playerData.getCurrent().setSneaking(false);
                    break;
                case START_SPRINTING:
                    playerData.getCurrent().setSprinting(true);
                    break;
                case STOP_SPRINTING:
                    playerData.getCurrent().setSprinting(false);
                    break;
                case START_FALL_FLYING:
                    playerData.getCurrent().setElytraFlying(true);
                    break;
                default:
                    break;
            }
        });

        return true;
    }

    @Override
    public void onStop(
        final @NotNull HACPlayer player,
        final @NotNull EntityActionPacket packet
    ) {
        throw new NotImplementedException("Updater Class.");
    }
}
