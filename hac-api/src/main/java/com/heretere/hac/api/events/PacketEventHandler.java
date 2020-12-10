package com.heretere.hac.api.events;

import com.google.common.collect.Sets;
import com.heretere.hac.api.player.HACPlayer;

import java.util.Comparator;
import java.util.Set;

public final class PacketEventHandler {
    private final Set<AbstractPacketEventExecutor<?>> executors;

    public PacketEventHandler() {
        this.executors = Sets.newTreeSet(
                Comparator.<AbstractPacketEventExecutor<?>, Priority>comparing(AbstractPacketEventExecutor::getPriority)
                        .thenComparing(AbstractPacketEventExecutor::getIdentifier)
        );
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
        this.executors.add(executor);
    }

    public void removeExecutor(AbstractPacketEventExecutor<?> executor) {
        this.executors.remove(executor);
    }
}
