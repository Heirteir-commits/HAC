package com.heretere.hac.core.player.data.player.executors;

import com.heretere.hac.api.events.types.Priority;
import com.heretere.hac.api.events.types.packets.AbstractPacketEventExecutor;
import com.heretere.hac.api.events.types.packets.wrapper.clientside.EntityActionPacket;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.core.player.data.player.PlayerData;
import org.apache.commons.lang.NotImplementedException;
import org.jetbrains.annotations.NotNull;

public final class PlayerDataEntityActionExecutor extends AbstractPacketEventExecutor<EntityActionPacket> {

    public PlayerDataEntityActionExecutor() {
        super(EntityActionPacket.class, Priority.PROCESS_1);
    }

    @Override
    protected boolean update(HACPlayer player, @NotNull EntityActionPacket packet) {
        PlayerData data = player.getDataManager().getData(PlayerData.class);

        switch (packet.getAction()) {
            case START_SNEAKING:
                data.getCurrent().setSneaking(true);
                break;
            case STOP_SNEAKING:
                data.getCurrent().setSneaking(false);
                break;
            case START_SPRINTING:
                data.getCurrent().setSprinting(true);
                break;
            case STOP_SPRINTING:
                data.getCurrent().setSprinting(false);
                break;
            case START_FALL_FLYING:
                data.getCurrent().setElytraFlying(true);
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    protected void onStop(HACPlayer player, @NotNull EntityActionPacket packet) {
        throw new NotImplementedException();
    }
}
