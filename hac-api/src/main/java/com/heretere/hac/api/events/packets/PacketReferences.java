package com.heretere.hac.api.events.packets;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.heretere.hac.api.events.packets.factory.PacketFactory;
import com.heretere.hac.api.events.packets.wrapper.WrappedPacket;
import com.heretere.hac.api.events.packets.wrapper.clientside.AbilitiesPacket;
import com.heretere.hac.api.events.packets.wrapper.clientside.EntityActionPacket;
import com.heretere.hac.api.events.packets.wrapper.clientside.FlyingPacket;
import com.heretere.hac.api.events.packets.wrapper.serverside.EntityVelocityPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

/**
 * Packet References are used to attach the version proxy wrapped packet factories to a type at runtime.
 */
public final class PacketReferences {
    /**
     * All the packets that are sent from the client to the server.
     */
    private final @NotNull ClientSide clientSide;
    /**
     * All the packets that are sent to the client from the server.
     */
    private final @NotNull ServerSide serverSide;

    /**
     * Instantiates a new Packet references.
     */
    public PacketReferences() {
        this.clientSide = new ClientSide();
        this.serverSide = new ServerSide();
    }

    /**
     * Gets client side.
     *
     * @return the client side
     */
    public @NotNull ClientSide getClientSide() {
        return this.clientSide;
    }

    /**
     * Gets server side.
     *
     * @return the server side
     */
    public @NotNull ServerSide getServerSide() {
        return this.serverSide;
    }

    /**
     * The type packet reference holder.
     */
    public abstract static class PacketReferenceHolder {
        /**
         * A map of the packet types stored by this reference holder.
         */
        private final @NotNull Map<Class<?>, PacketReference<?>> packetReferences;

        /**
         * Instantiates a new packet reference holder.
         */
        protected PacketReferenceHolder() {
            this.packetReferences = Maps.newIdentityHashMap();
        }

        /**
         * Get packet reference.
         *
         * @param nmsClass the nms class
         * @return the packet reference
         */
        public final @NotNull Optional<PacketReference<?>> get(final @NotNull Class<?> nmsClass) {
            return Optional.ofNullable(this.packetReferences.get(nmsClass));
        }

        /**
         * Register.
         *
         * @param packetReference the packet reference
         * @param builder         the builder
         */
        protected void register(
            final @NotNull PacketReference<?> packetReference,
            final @NotNull PacketFactory<?> builder
        ) {
            for (Class<?> nmsClass : builder.getPacketClasses()) {
                this.packetReferences.put(nmsClass, packetReference);
            }
        }
    }

    /**
     * The type Client side.
     */
    public static final class ClientSide extends PacketReferenceHolder {
        /**
         * Abilities Packet reference holder.
         */
        private final @NotNull PacketReference<AbilitiesPacket> abilities;
        /**
         * Entity Action Packet reference holder.
         */
        private final @NotNull PacketReference<EntityActionPacket> entityAction;
        /**
         * Flying Packet reference holder.
         */
        private final @NotNull PacketReference<FlyingPacket> flying;

        private ClientSide() {
            super();
            this.abilities = new PacketReference<>("abilities", this, AbilitiesPacket.class);
            this.entityAction = new PacketReference<>("entity_action", this, EntityActionPacket.class);
            this.flying = new PacketReference<>("flying", this, FlyingPacket.class);
        }

        /**
         * Gets abilities.
         *
         * @return the abilities
         */
        public @NotNull PacketReference<AbilitiesPacket> getAbilities() {
            return this.abilities;
        }

        /**
         * Gets entity action.
         *
         * @return the entity action
         */
        public @NotNull PacketReference<EntityActionPacket> getEntityAction() {
            return this.entityAction;
        }

        /**
         * Gets flying.
         *
         * @return the flying
         */
        public @NotNull PacketReference<FlyingPacket> getFlying() {
            return this.flying;
        }
    }

    /**
     * The type Server side.
     */
    public static final class ServerSide extends PacketReferenceHolder {
        /**
         * Entity velocity packet reference holder.
         */
        private final @NotNull PacketReference<EntityVelocityPacket> entityVelocity;

        private ServerSide() {
            super();
            this.entityVelocity = new PacketReference<>("entity_velocity", this, EntityVelocityPacket.class);
        }

        /**
         * Gets entity velocity.
         *
         * @return the entity velocity
         */
        public @NotNull PacketReference<EntityVelocityPacket> getEntityVelocity() {
            return this.entityVelocity;
        }
    }

    /**
     * The type Packet reference.
     *
     * @param <T> the type parameter
     */
    public static final class PacketReference<T extends WrappedPacket> {
        /**
         * A unique identifier that identifies the type of this packet reference.
         * This identifier is attached to all Event Executors. in the format of
         * base_identifier + identifier.
         */
        private final @NotNull String identifier;
        /**
         * The parent reference holder class.
         */
        private final @NotNull PacketReferenceHolder parent;
        /**
         * The wrapped packet class this reference pertains to.
         */
        private final @NotNull Class<T> wrappedPacketClass;
        /**
         * The factory that creates wrapped packets of this type.
         */
        private @Nullable PacketFactory<T> builder;

        /**
         * Ensures that only one factory is registered.
         */
        private boolean registered = false;

        private PacketReference(
            final @NotNull String identifier,
            final @NotNull PacketReferenceHolder parent,
            final @NotNull Class<T> wrappedPacketClass
        ) {
            this.identifier = identifier;
            this.parent = parent;
            this.wrappedPacketClass = wrappedPacketClass;
        }

        /**
         * Register.
         *
         * @param builder the builder
         */
        public void register(final @NotNull PacketFactory<T> builder) {
            Preconditions.checkState(!this.registered, "Already registered.");
            Preconditions.checkArgument(
                builder.getWrappedClass().equals(this.wrappedPacketClass),
                "PacketBuilder class not of same type of PacketReference."
            );
            this.builder = builder;
            this.parent.register(this, this.builder);
            this.registered = true;
        }


        /**
         * Gets wrapped packet class.
         *
         * @return the wrapped packet class
         */
        public @NotNull Class<T> getWrappedPacketClass() {
            return this.wrappedPacketClass;
        }

        /**
         * Gets builder.
         *
         * @return the builder
         */
        public @NotNull PacketFactory<T> getBuilder() {
            Preconditions.checkState(this.registered, "Not registered.");
            assert this.builder != null;
            return this.builder;
        }

        /**
         * Gets identifier.
         *
         * @return the identifier
         */
        public @NotNull String getIdentifier() {
            return this.identifier;
        }
    }
}
