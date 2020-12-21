package com.heretere.hac.api.events.packets;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.heretere.hac.api.events.packets.factory.AbstractPacketFactory;
import com.heretere.hac.api.events.packets.wrapper.WrappedPacket;
import com.heretere.hac.api.events.packets.wrapper.clientside.AbilitiesPacket;
import com.heretere.hac.api.events.packets.wrapper.clientside.EntityActionPacket;
import com.heretere.hac.api.events.packets.wrapper.clientside.FlyingPacket;
import com.heretere.hac.api.events.packets.wrapper.serverside.EntityVelocityPacket;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Packet References are used to attach the version proxy wrapped packet factories to a type at runtime.
 */
public final class PacketReferences {
    /**
     * All the packets that are sent from the client to the server.
     */
    private final ClientSide clientSide;
    /**
     * All the packets that are sent to the client from the server.
     */
    private final ServerSide serverSide;

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
    public ClientSide getClientSide() {
        return this.clientSide;
    }

    /**
     * Gets server side.
     *
     * @return the server side
     */
    public ServerSide getServerSide() {
        return this.serverSide;
    }

    /**
     * The type Abstract packet reference holder.
     */
    public abstract static class AbstractPacketReferenceHolder {
        /**
         * A map of the packet types stored by this reference holder.
         */
        private final Map<Class<?>, PacketReference<?>> packetReferences;

        /**
         * Instantiates a new Abstract packet reference holder.
         */
        protected AbstractPacketReferenceHolder() {
            this.packetReferences = Maps.newIdentityHashMap();
        }

        /**
         * Get packet reference.
         *
         * @param nmsClass the nms class
         * @return the packet reference
         */
        public final PacketReference<?> get(@NotNull final Class<?> nmsClass) {
            return this.packetReferences.get(nmsClass);
        }

        /**
         * Register.
         *
         * @param packetReference the packet reference
         * @param builder         the builder
         */
        protected void register(@NotNull final PacketReference<?> packetReference,
                                @NotNull final AbstractPacketFactory<?> builder) {
            for (Class<?> nmsClass : builder.getPacketClasses()) {
                this.packetReferences.put(nmsClass, packetReference);
            }
        }
    }

    /**
     * The type Client side.
     */
    public static final class ClientSide extends AbstractPacketReferenceHolder {
        /**
         * Abilities Packet reference holder.
         */
        private final PacketReference<AbilitiesPacket> abilities;
        /**
         * Entity Action Packet reference holder.
         */
        private final PacketReference<EntityActionPacket> entityAction;
        /**
         * Flying Packet reference holder.
         */
        private final PacketReference<FlyingPacket> flying;

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
        public PacketReference<AbilitiesPacket> getAbilities() {
            return this.abilities;
        }

        /**
         * Gets entity action.
         *
         * @return the entity action
         */
        public PacketReference<EntityActionPacket> getEntityAction() {
            return this.entityAction;
        }

        /**
         * Gets flying.
         *
         * @return the flying
         */
        public PacketReference<FlyingPacket> getFlying() {
            return this.flying;
        }
    }

    /**
     * The type Server side.
     */
    public static final class ServerSide extends AbstractPacketReferenceHolder {
        /**
         * Entity velocity packet reference holder.
         */
        private final PacketReference<EntityVelocityPacket> entityVelocity;

        private ServerSide() {
            super();
            this.entityVelocity = new PacketReference<>("entity_velocity", this, EntityVelocityPacket.class);
        }

        /**
         * Gets entity velocity.
         *
         * @return the entity velocity
         */
        public PacketReference<EntityVelocityPacket> getEntityVelocity() {
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
        private final String identifier;
        /**
         * The parent reference holder class.
         */
        private final AbstractPacketReferenceHolder parent;
        /**
         * The wrapped packet class this reference pertains to.
         */
        private final Class<T> wrappedPacketClass;
        /**
         * The factory that creates wrapped packets of this type.
         */
        private AbstractPacketFactory<T> builder;

        /**
         * Ensures that only one factory is registered.
         */
        private boolean registered = false;

        private PacketReference(@NotNull final String identifier,
                                @NotNull final AbstractPacketReferenceHolder parent,
                                @NotNull final Class<T> wrappedPacketClass) {
            this.identifier = identifier;
            this.parent = parent;
            this.wrappedPacketClass = wrappedPacketClass;
        }

        /**
         * Register.
         *
         * @param builder the builder
         */
        public void register(@NotNull final AbstractPacketFactory<T> builder) {
            Preconditions.checkState(!this.registered, "Already registered.");
            Preconditions.checkArgument(builder.getWrappedClass().equals(this.wrappedPacketClass),
                    "PacketBuilder class not of same type of PacketReference.");
            this.builder = builder;
            this.parent.register(this, this.builder);
            this.registered = true;
        }


        /**
         * Gets wrapped packet class.
         *
         * @return the wrapped packet class
         */
        public Class<T> getWrappedPacketClass() {
            return this.wrappedPacketClass;
        }

        /**
         * Gets builder.
         *
         * @return the builder
         */
        public AbstractPacketFactory<T> getBuilder() {
            Preconditions.checkState(this.registered, "Not registered.");
            return this.builder;
        }

        /**
         * Gets identifier.
         *
         * @return the identifier
         */
        public String getIdentifier() {
            return identifier;
        }
    }
}
