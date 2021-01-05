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

package com.heretere.hac.core.proxy.versions.sixteen.packets.builder.clientside;

import com.heretere.hac.api.packet.factory.PacketFactory;
import com.heretere.hac.api.packet.wrapper.clientside.EntityActionPacket;
import com.heretere.hac.api.player.HACPlayer;
import net.minecraft.server.v1_16_R3.PacketPlayInEntityAction;
import org.jetbrains.annotations.NotNull;

/**
 * The type Entity action packet factory.
 */
public final class EntityActionPacketFactory extends PacketFactory<EntityActionPacket> {
    /**
     * Instantiates a new Entity action packet factory.
     */
    public EntityActionPacketFactory() {
        super(PacketPlayInEntityAction.class);
    }

    @Override
    public @NotNull EntityActionPacket create(
        final @NotNull HACPlayer player,
        final @NotNull Object packet
    ) {
        PacketPlayInEntityAction entityAction = (PacketPlayInEntityAction) packet;

        EntityActionPacket.Action action;

        switch (entityAction.c()) {
            case PRESS_SHIFT_KEY:
                action = EntityActionPacket.Action.START_SNEAKING;
                break;
            case RELEASE_SHIFT_KEY:
                action = EntityActionPacket.Action.STOP_SNEAKING;
                break;
            case START_SPRINTING:
                action = EntityActionPacket.Action.START_SPRINTING;
                break;
            case STOP_SPRINTING:
                action = EntityActionPacket.Action.STOP_SPRINTING;
                break;
            default:
                action = EntityActionPacket.Action.INVALID;
        }

        return new EntityActionPacket(action);
    }

    @Override
    public @NotNull Class<EntityActionPacket> getWrappedClass() {
        return EntityActionPacket.class;
    }
}
