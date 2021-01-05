/*
 * MIT License
 *
 * Copyright (c) 2021 Justin Heflin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.heretere.hac.api.packet;

import com.heretere.hac.api.packet.wrapper.clientside.AbilitiesPacket;
import com.heretere.hac.api.packet.wrapper.clientside.EntityActionPacket;
import com.heretere.hac.api.packet.wrapper.clientside.FlyingPacket;
import com.heretere.hac.api.packet.wrapper.serverside.EntityVelocityPacket;
import org.jetbrains.annotations.NotNull;

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

}
