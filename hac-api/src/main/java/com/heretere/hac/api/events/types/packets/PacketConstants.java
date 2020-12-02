package com.heretere.hac.api.events.types.packets;

import com.heretere.hac.api.events.types.packets.wrapper.AbstractWrappedPacketIn;
import com.heretere.hac.api.events.types.packets.wrapper.AbstractWrappedPacketOut;
import com.heretere.hac.api.events.types.packets.wrapper.clientside.AbilitiesPacket;
import com.heretere.hac.api.events.types.packets.wrapper.clientside.EntityActionPacket;
import com.heretere.hac.api.events.types.packets.wrapper.clientside.FlyingPacket;
import com.heretere.hac.api.events.types.packets.wrapper.serverside.EntityVelocityPacket;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * Stores all the Packet Types that are currently created for the API.
 */
public final class PacketConstants {
    private PacketConstants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Gets packet type from string.
     *
     * @param <T>       the type parameter
     * @param direction the direction
     * @param input     the input
     * @return the packet type from string
     */
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

    /**
     * The enum Out.
     */
    public enum Out {
        /**
         * Entity velocity out.
         */
        ENTITY_VELOCITY(EntityVelocityPacket.class, "PacketPlayOutEntityVelocity"),
        /**
         * Unknown out.
         */
        UNKNOWN(null);

        private final Class<? extends AbstractWrappedPacketOut> wrappedClass;
        private final ImmutableSet<String> names;

        Out(Class<? extends AbstractWrappedPacketOut> wrappedClass, String... names) {
            this.wrappedClass = wrappedClass;
            this.names = ImmutableSet.copyOf(names);
        }

        /**
         * Gets wrapped class.
         *
         * @return the wrapped class
         */
        public Class<? extends AbstractWrappedPacketOut> getWrappedClass() {
            return wrappedClass;
        }
    }

    /**
     * The enum In.
     */
    public enum In {
        /**
         * Flying in.
         */
        FLYING(FlyingPacket.class, "PacketPlayInFlying", "PacketPlayInPositionLook", "PacketPlayInPosition", "PacketPlayInLook"),
        /**
         * Abilities in.
         */
        ABILITIES(AbilitiesPacket.class, "PacketPlayInAbilities"),
        /**
         * Entity action in.
         */
        ENTITY_ACTION(EntityActionPacket.class, "PacketPlayInEntityAction"),
        /**
         * Unknown in.
         */
        UNKNOWN(null);

        private final Class<? extends AbstractWrappedPacketIn> wrappedClass;
        private final ImmutableSet<String> names;

        In(Class<? extends AbstractWrappedPacketIn> wrappedClass, String... names) {
            this.wrappedClass = wrappedClass;
            this.names = ImmutableSet.copyOf(names);
        }

        /**
         * Gets wrapped class.
         *
         * @return the wrapped class
         */
        public Class<? extends AbstractWrappedPacketIn> getWrappedClass() {
            return wrappedClass;
        }
    }
}
