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

import com.google.common.base.Preconditions;
import com.heretere.hac.api.packet.factory.PacketFactory;
import com.heretere.hac.api.packet.wrapper.WrappedPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The type Packet reference.
 *
 * @param <T> the type parameter
 */
public final class PacketReference<T extends WrappedPacket> {
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

    PacketReference(
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
