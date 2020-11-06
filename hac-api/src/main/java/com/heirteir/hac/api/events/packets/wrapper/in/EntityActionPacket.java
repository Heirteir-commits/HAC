package com.heirteir.hac.api.events.packets.wrapper.in;

import com.heirteir.hac.api.events.packets.Packet;
import com.heirteir.hac.api.events.packets.wrapper.AbstractWrappedPacketIn;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

//PacketPlayInEntityAction
@Getter
public final class EntityActionPacket extends AbstractWrappedPacketIn {
    private Action action;

    public EntityActionPacket() {
        super(Packet.In.ENTITY_ACTION);
    }

    @RequiredArgsConstructor
    @Getter
    public enum Action {
        START_SNEAKING,
        STOP_SNEAKING,
        START_SPRINTING,
        STOP_SPRINTING,
        START_FALL_FLYING,
        INVALID;

        public static Action fromString(String name) {
            return Arrays.stream(Action.values()).filter(action -> action.name().equals(name)).findFirst().orElse(INVALID);
        }
    }
}
