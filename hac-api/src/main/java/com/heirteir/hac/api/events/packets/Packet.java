package com.heirteir.hac.api.events.packets;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.heirteir.hac.api.events.packets.wrapper.AbstractWrappedPacketIn;
import com.heirteir.hac.api.events.packets.wrapper.AbstractWrappedPacketOut;
import com.heirteir.hac.api.events.packets.wrapper.in.AbilitiesPacket;
import com.heirteir.hac.api.events.packets.wrapper.in.EntityActionPacket;
import com.heirteir.hac.api.events.packets.wrapper.in.FlyingPacket;
import com.heirteir.hac.api.events.packets.wrapper.out.EntityVelocityPacket;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public final class Packet {


    public static <T> T getPacketTypeFromString(@NotNull Class<T> direction, @NotNull String input) {
        boolean in = direction.equals(In.class);
        boolean out = direction.equals(Out.class);

        Preconditions.checkArgument(in || out, "direction");

        if (in) {
            return direction.cast(Arrays.stream(In.values())
                    .filter(type -> type.names.contains(input))
                    .findFirst()
                    .orElse(In.UNKNOWN));
        } else {
            return direction.cast(Arrays.stream(Out.values())
                    .filter(type -> type.names.contains(input))
                    .findFirst()
                    .orElse(Out.UNKNOWN));
        }
    }

    @Getter
    public enum Out {
        ENTITY_VELOCITY(EntityVelocityPacket.class, "PacketPlayOutEntityVelocity"),
        UNKNOWN(null);

        private final Class<? extends AbstractWrappedPacketOut> wrappedClass;
        private final ImmutableSet<String> names;

        Out(Class<? extends AbstractWrappedPacketOut> wrappedClass, String... names) {
            this.wrappedClass = wrappedClass;
            this.names = ImmutableSet.copyOf(names);
        }
    }

    @Getter
    public enum In {
        FLYING(FlyingPacket.class, "PacketPlayInFlying", "PacketPlayInPositionLook", "PacketPlayInPosition", "PacketPlayInLook"),
        ABILITIES(AbilitiesPacket.class, "PacketPlayInAbilities"),
        ENTITY_ACTION(EntityActionPacket.class, "PacketPlayInEntityAction"),
        UNKNOWN(null);

        private final Class<? extends AbstractWrappedPacketIn> wrappedClass;
        private final ImmutableSet<String> names;

        In(Class<? extends AbstractWrappedPacketIn> wrappedClass, String... names) {
            this.wrappedClass = wrappedClass;
            this.names = ImmutableSet.copyOf(names);
        }
    }
}
