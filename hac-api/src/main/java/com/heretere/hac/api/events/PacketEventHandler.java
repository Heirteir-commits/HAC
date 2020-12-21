package com.heretere.hac.api.events;

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
    private static final Comparator<AbstractPacketEventExecutor<?>> COMPARATOR =
            Comparator.<AbstractPacketEventExecutor<?>, Priority>
                    comparing(AbstractPacketEventExecutor::getPriority)
                    .thenComparing(AbstractPacketEventExecutor::getIdentifier);

    /**
     * The executors attached to this handler.
     */
    private Set<AbstractPacketEventExecutor<?>> executors;

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
    public void execute(@NotNull final HACPlayer player, @NotNull final Object wrappedPacket) {
        for (AbstractPacketEventExecutor<?> executor : this.executors) {
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
    public void addExecutor(@NotNull final AbstractPacketEventExecutor<?> executor) {
        Set<AbstractPacketEventExecutor<?>> set = this.tempTreeSet();
        set.add(executor);

        this.executors = ImmutableSortedSet.copyOf(PacketEventHandler.COMPARATOR, set);
    }

    /**
     * Remove executor.
     *
     * @param executor the executor
     */
    public void removeExecutor(@NotNull final AbstractPacketEventExecutor<?> executor) {
        Set<AbstractPacketEventExecutor<?>> set = this.tempTreeSet();
        set.remove(executor);

        this.executors = ImmutableSortedSet.copyOf(PacketEventHandler.COMPARATOR, set);
    }

    private Set<AbstractPacketEventExecutor<?>> tempTreeSet() {
        Set<AbstractPacketEventExecutor<?>> set = Sets.newTreeSet(PacketEventHandler.COMPARATOR);
        set.addAll(this.executors);
        return set;
    }
}
