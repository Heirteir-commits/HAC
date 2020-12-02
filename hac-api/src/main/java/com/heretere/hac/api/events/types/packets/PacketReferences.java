package com.heretere.hac.api.events.types.packets;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.heretere.hac.api.events.types.packets.builder.PacketBuilder;
import com.heretere.hac.api.events.types.packets.wrapper.WrappedPacket;
import com.heretere.hac.api.events.types.packets.wrapper.clientside.AbilitiesPacket;
import com.heretere.hac.api.events.types.packets.wrapper.clientside.EntityActionPacket;
import com.heretere.hac.api.events.types.packets.wrapper.clientside.FlyingPacket;
import com.heretere.hac.api.events.types.packets.wrapper.serverside.EntityVelocityPacket;

import java.util.Map;

public final class PacketReferences {
    private final ClientSide clientSide;
    private final ServerSide serverSide;

    public PacketReferences() {
        this.clientSide = new ClientSide();
        this.serverSide = new ServerSide();
    }

    public ClientSide getClientSide() {
        return this.clientSide;
    }

    public ServerSide getServerSide() {
        return this.serverSide;
    }

    public abstract static class PacketReferenceHolder {
        private final Map<Class<?>, PacketReference<?>> packetReferences;

        protected PacketReferenceHolder() {
            this.packetReferences = Maps.newHashMap();
        }

        public final PacketReference<?> get(Class<?> nmsClass) {
            return this.packetReferences.get(nmsClass);
        }

        private void register(PacketReference<?> packetReference, PacketBuilder<?> builder) {
            for (Class<?> nmsClass : builder.getPacketClasses()) {
                this.packetReferences.put(nmsClass, packetReference);
            }
        }
    }

    public static final class ClientSide extends PacketReferenceHolder {
        private final PacketReference<AbilitiesPacket> abilities;
        private final PacketReference<EntityActionPacket> entityAction;
        private final PacketReference<FlyingPacket> flying;

        private ClientSide() {
            super();
            this.abilities = new PacketReference<>(this, AbilitiesPacket.class);
            this.entityAction = new PacketReference<>(this, EntityActionPacket.class);
            this.flying = new PacketReference<>(this, FlyingPacket.class);
        }

        public PacketReference<AbilitiesPacket> getAbilities() {
            return this.abilities;
        }

        public PacketReference<EntityActionPacket> getEntityAction() {
            return this.entityAction;
        }

        public PacketReference<FlyingPacket> getFlying() {
            return this.flying;
        }
    }

    public static final class ServerSide extends PacketReferenceHolder {
        private final PacketReference<EntityVelocityPacket> entityVelocity;

        private ServerSide() {
            super();
            this.entityVelocity = new PacketReference<>(this, EntityVelocityPacket.class);
        }

        public PacketReference<EntityVelocityPacket> getEntityVelocity() {
            return this.entityVelocity;
        }
    }

    public static final class PacketReference<T extends WrappedPacket> {
        private final PacketReferenceHolder parent;
        private final Class<T> wrappedPacketClass;
        private PacketBuilder<T> builder;

        private boolean registered = false;

        private PacketReference(PacketReferenceHolder parent, Class<T> wrappedPacketClass) {
            this.parent = parent;
            this.wrappedPacketClass = wrappedPacketClass;
        }

        public void register(PacketBuilder<T> builder) {
            Preconditions.checkState(!this.registered, "Already registered.");
            Preconditions.checkArgument(builder.getWrappedClass().equals(this.wrappedPacketClass),
                    "PacketBuilder class not of same type of PacketReference.");
            this.builder = builder;
            this.parent.register(this, this.builder);
            this.registered = true;
        }


        public Class<T> getWrappedPacketClass() {
            return this.wrappedPacketClass;
        }

        public PacketBuilder<T> getBuilder() {
            Preconditions.checkState(this.registered, "Not registered.");
            return this.builder;
        }
    }
}
