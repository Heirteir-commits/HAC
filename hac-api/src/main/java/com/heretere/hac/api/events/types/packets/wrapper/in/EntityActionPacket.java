package com.heretere.hac.api.events.types.packets.wrapper.in;

import com.heretere.hac.api.events.types.packets.PacketConstants;
import com.heretere.hac.api.events.types.packets.wrapper.AbstractWrappedPacketIn;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

//PacketPlayInEntityAction
@Getter
public final class EntityActionPacket extends AbstractWrappedPacketIn {
    public static final EntityActionPacket DEFAULT;

    static {
        DEFAULT = new EntityActionPacket();
        DEFAULT.action = Action.INVALID;
    }

    private Action action;

    public EntityActionPacket() {
        super(PacketConstants.In.ENTITY_ACTION);
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
