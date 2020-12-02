package com.heretere.hac.core.player.data.location.updaters;

import com.heretere.hac.api.events.types.Priority;
import com.heretere.hac.api.events.types.packets.AbstractPacketEventExecutor;
import com.heretere.hac.api.events.types.packets.wrapper.in.EntityActionPacket;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.core.player.data.location.PlayerData;
import org.apache.commons.lang.NotImplementedException;
import org.jetbrains.annotations.NotNull;

public final class PlayerDataEntityActionUpdater extends AbstractPacketEventExecutor<EntityActionPacket> {
    public PlayerDataEntityActionUpdater() {
        super(EntityActionPacket.class, Priority.PROCESS_1);
    }

    @Override
    protected boolean update(HACPlayer player, @NotNull EntityActionPacket packet) {
        PlayerData data = player.getDataManager().getData(PlayerData.class);
        switch (packet.getAction()) {
            case START_SNEAKING:
                data.setSneaking(true);
                break;
            case STOP_SNEAKING:
                data.setSneaking(false);
                break;
            case START_FALL_FLYING:
                data.setElytraFlying(true);
                break;
            case START_SPRINTING:
                data.setSprinting(true);
                break;
            case STOP_SPRINTING:
                data.setSprinting(false);
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
