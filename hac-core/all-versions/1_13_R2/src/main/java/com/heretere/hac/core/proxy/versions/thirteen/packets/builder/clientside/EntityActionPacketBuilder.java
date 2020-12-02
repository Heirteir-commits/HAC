package com.heretere.hac.core.proxy.versions.thirteen.packets.builder.clientside;

import com.heretere.hac.api.events.types.packets.builder.PacketBuilder;
import com.heretere.hac.api.events.types.packets.wrapper.clientside.EntityActionPacket;
import com.heretere.hac.api.player.HACPlayer;
import net.minecraft.server.v1_13_R2.PacketPlayInEntityAction;

public final class EntityActionPacketBuilder extends PacketBuilder<EntityActionPacket> {
    public EntityActionPacketBuilder() {
        super(PacketPlayInEntityAction.class);
    }

    @Override
    public EntityActionPacket create(HACPlayer player, Object packet) {
        PacketPlayInEntityAction entityAction = (PacketPlayInEntityAction) packet;

        EntityActionPacket.Action action;

        switch (entityAction.c()) {
            case START_SNEAKING:
                action = EntityActionPacket.Action.START_SNEAKING;
                break;
            case STOP_SNEAKING:
                action = EntityActionPacket.Action.STOP_SNEAKING;
                break;
            case START_SPRINTING:
                action = EntityActionPacket.Action.START_SPRINTING;
                break;
            case STOP_SPRINTING:
                action = EntityActionPacket.Action.STOP_SPRINTING;
                break;
            case START_FALL_FLYING:
                action = EntityActionPacket.Action.START_FALL_FLYING;
                break;
            default:
                action = EntityActionPacket.Action.INVALID;
        }

        return new EntityActionPacket(
                action
        );
    }

    @Override
    public Class<EntityActionPacket> getWrappedClass() {
        return EntityActionPacket.class;
    }
}
