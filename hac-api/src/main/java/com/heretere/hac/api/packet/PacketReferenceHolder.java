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

import com.google.common.collect.Maps;
import com.heretere.hac.api.packet.factory.PacketFactory;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

/**
 * The type packet reference holder.
 */
public abstract class PacketReferenceHolder {
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
    public final @NotNull Optional<PacketReference<?>> getReferenceFromNMSClass(final @NotNull Class<?> nmsClass) {
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
