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

package com.heretere.hac.api.event;

import com.heretere.hac.api.event.packet.PacketReferences;
import com.heretere.hac.api.event.packet.wrapper.WrappedPacket;
import com.heretere.hac.api.player.HACPlayer;
import org.jetbrains.annotations.NotNull;

public abstract class PacketEventExecutor<T extends WrappedPacket> {
    /**
     * The run priority of the event executor.
     */
    private final @NotNull Priority priority;
    /**
     * The unique identifier for this event executor.
     */
    private final @NotNull String identifier;

    /**
     * The WrappedPacket class this executor handles.
     */
    private final @NotNull Class<T> wrappedClass;

    /**
     * True if this executor is ran on the main server thread.
     */
    private final boolean sync;

    /**
     * Creates a new PacketEventExecutor.
     *
     * @param priority   the priority of this event.
     * @param identifier the unique identifier for this event. This is used to determine equality by the event system.
     * @param reference  The packet reference this packet event executor is using.
     * @param sync       true if ran on the main server thread, false if async.
     */
    protected PacketEventExecutor(
        final @NotNull Priority priority,
        final @NotNull String identifier,
        final @NotNull PacketReferences.PacketReference<T> reference,
        final boolean sync
    ) {
        this.priority = priority;
        this.identifier = identifier + "_" + reference.getIdentifier();
        this.wrappedClass = reference.getWrappedPacketClass();
        this.sync = sync;
    }

    /**
     * This ensures that the passed in packet is of the correct type then delegates it to the subclass.
     *
     * @param player The HACPlayer
     * @param packet The Packet to be passed into the executor
     * @return true if execution was successful
     */
    public final boolean execute(
        final @NotNull HACPlayer player,
        final @NotNull Object packet
    ) {
        return this.execute(player, this.wrappedClass.cast(packet));
    }

    /**
     * This ensures that the passed in packet is of the correct type then delegates it to the subclass.
     *
     * @param player The HACPlayer
     * @param packet The packet to be passed into the executor
     */
    public final void onStop(
        final @NotNull HACPlayer player,
        final @NotNull Object packet
    ) {
        this.onStop(player, this.wrappedClass.cast(packet));
    }

    protected abstract boolean execute(
        @NotNull HACPlayer player,
        @NotNull T packet
    );

    protected abstract void onStop(
        @NotNull HACPlayer player,
        @NotNull T packet
    );

    /**
     * The execution priority for the event executor.
     *
     * @return The priority
     */
    public @NotNull Priority getPriority() {
        return this.priority;
    }

    /**
     * The unique identifier for the event executor.
     *
     * @return The unique identifier
     */
    public @NotNull String getIdentifier() {
        return this.identifier;
    }

    /**
     * The wrapped packet class this executor handles.
     *
     * @return Wrapped packet class
     */
    public @NotNull Class<T> getWrappedClass() {
        return this.wrappedClass;
    }


    /**
     * @return true if ran on the main thread, false otherwise.
     */
    public final boolean isSync() {
        return this.sync;
    }
}
