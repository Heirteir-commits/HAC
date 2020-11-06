package com.heirteir.hac.core.player.data.location.updaters;

import com.heirteir.hac.api.events.AbstractPacketEvent;
import com.heirteir.hac.api.events.packets.wrapper.in.EntityActionPacket;
import com.heirteir.hac.api.player.HACPlayer;
import com.heirteir.hac.core.player.data.location.PlayerData;
import org.apache.commons.lang.NotImplementedException;
import org.jetbrains.annotations.NotNull;

public final class EntityActionUpdater extends AbstractPacketEvent<EntityActionPacket> {
    public EntityActionUpdater() {
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
