package com.heretere.hac.api.events.types.packets;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.heretere.hac.api.events.types.packets.wrapper.AbstractWrappedPacketIn;
import com.heretere.hac.api.events.types.packets.wrapper.AbstractWrappedPacketOut;
import com.heretere.hac.api.events.types.packets.wrapper.clientside.AbilitiesPacket;
import com.heretere.hac.api.events.types.packets.wrapper.clientside.EntityActionPacket;
import com.heretere.hac.api.events.types.packets.wrapper.clientside.FlyingPacket;
import com.heretere.hac.api.events.types.packets.wrapper.serverside.EntityVelocityPacket;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public final class PacketConstants {


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
