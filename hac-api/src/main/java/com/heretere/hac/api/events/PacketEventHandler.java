package com.heretere.hac.api.events;

import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Sets;
import com.heretere.hac.api.player.HACPlayer;

import java.util.Comparator;
import java.util.Set;

public final class PacketEventHandler {
    private static final Comparator<AbstractPacketEventExecutor<?>> COMPARATOR =
            Comparator.<AbstractPacketEventExecutor<?>, Priority>
                    comparing(AbstractPacketEventExecutor::getPriority)
                    .thenComparing(AbstractPacketEventExecutor::getIdentifier);

    private Set<AbstractPacketEventExecutor<?>> executors;

    public PacketEventHandler() {
        this.executors = ImmutableSortedSet.of();
    }

    public void execute(HACPlayer player, Object wrappedPacket) {
        for (AbstractPacketEventExecutor<?> executor : this.executors) {
            if (!executor.execute(player, wrappedPacket)) {
                executor.onStop(player, wrappedPacket);
                break;
            }
        }
    }

    public void addExecutor(AbstractPacketEventExecutor<?> executor) {
        Set<AbstractPacketEventExecutor<?>> set = this.tempTreeSet();
        set.add(executor);

        this.executors = ImmutableSortedSet.copyOf(PacketEventHandler.COMPARATOR, set);
    }

    public void removeExecutor(AbstractPacketEventExecutor<?> executor) {
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
