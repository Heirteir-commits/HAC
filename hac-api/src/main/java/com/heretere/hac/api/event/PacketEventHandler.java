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

import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Sets;
import com.heretere.hac.api.player.HACPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Set;

/**
 * A Packet Event Handler is what handles all wrapped packets of a specific type.
 * <p>
 * Type safety is insured by the packet executors. They ensure that the wrapped packet is of the
 * correct type in execute() and onStop().
 */
public final class PacketEventHandler {
    /**
     * This comparator is used for sorting and equality. Classes are determined equal by their priority, and
     * identifier instead of .equals.
     */
    private static final @NotNull Comparator<PacketEventExecutor<?>> COMPARATOR =
        Comparator.<PacketEventExecutor<?>, Priority>comparing(
            PacketEventExecutor::getPriority).thenComparing(PacketEventExecutor::getIdentifier);

    /**
     * The executors attached to this handler.
     */
    private @NotNull Set<PacketEventExecutor<?>> executors;

    /**
     * The size of this PacketEventHandler.
     */
    private int size;

    /**
     * Instantiates a new Packet event handler.
     */
    public PacketEventHandler() {
        this.executors = ImmutableSortedSet.of();
    }

    /**
     * Executes all the event executors in the chain. If an event executor returns false, we stop the chain.
     *
     * @param player        the player
     * @param wrappedPacket the wrapped packet
     */
    public void execute(
        final @NotNull HACPlayer player,
        final @NotNull Object wrappedPacket
    ) {
        for (PacketEventExecutor<?> executor : this.executors) {
            if (!executor.execute(player, wrappedPacket)) {
                executor.onStop(player, wrappedPacket);
                break;
            }
        }
    }

    /**
     * Add an event executor to this handler.
     *
     * @param executor the executor
     */
    public void addExecutor(final @NotNull PacketEventExecutor<?> executor) {
        Set<PacketEventExecutor<?>> set = this.tempTreeSet();
        set.add(executor);

        this.executors = ImmutableSortedSet.copyOf(PacketEventHandler.COMPARATOR, set);
        this.size = this.executors.size();
    }

    /**
     * Remove executor.
     *
     * @param executor the executor
     */
    public void removeExecutor(final @NotNull PacketEventExecutor<?> executor) {
        Set<PacketEventExecutor<?>> set = this.tempTreeSet();
        set.remove(executor);

        this.executors = ImmutableSortedSet.copyOf(PacketEventHandler.COMPARATOR, set);
        this.size = this.executors.size();
    }

    private @NotNull Set<PacketEventExecutor<?>> tempTreeSet() {
        Set<PacketEventExecutor<?>> set = Sets.newTreeSet(PacketEventHandler.COMPARATOR);
        set.addAll(this.executors);
        return set;
    }

    /**
     * @return The amount of executors in this event executor.
     */
    public int getSize() {
        return this.size;
    }
}
