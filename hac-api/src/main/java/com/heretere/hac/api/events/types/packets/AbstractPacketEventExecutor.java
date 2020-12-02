package com.heretere.hac.api.events.types.packets;

import com.heretere.hac.api.events.types.Priority;
import com.heretere.hac.api.events.types.packets.wrapper.WrappedPacket;
import com.heretere.hac.api.player.HACPlayer;
import org.jetbrains.annotations.NotNull;

/**
 * The type Abstract packet event executor.
 *
 * @param <T> the type parameter
 */
public abstract class AbstractPacketEventExecutor<T extends WrappedPacket> {
    private final Class<T> wrappedClass;
    private final Priority priority;

    /**
     * Instantiates a new Abstract packet event executor.
     *
     * @param wrappedClass the wrapped class
     * @param priority     the priority
     */
    protected AbstractPacketEventExecutor(Class<T> wrappedClass, Priority priority) {
        this.wrappedClass = wrappedClass;
        this.priority = priority;
    }

    /**
     * Passes to {@link AbstractPacketEventExecutor#update(HACPlayer, WrappedPacket)}
     *
     * @param player the player
     * @param packet the packet should be of type {@link WrappedPacket}.
     * @return the boolean
     */
    public final boolean update(HACPlayer player, @NotNull Object packet) {
        return this.update(player, this.wrappedClass.cast(packet));
    }

    /**
     * Updates an Event Executor with the specified WrappedPacket
     *
     * @param player the player
     * @param packet the packet should be of type {@link WrappedPacket}.
     * @return the boolean
     */
    protected abstract boolean update(HACPlayer player, @NotNull T packet);

    /**
     * Passes to {@link AbstractPacketEventExecutor#onStop(HACPlayer, WrappedPacket)}.
     *
     * @param player the player
     * @param packet the {@link WrappedPacket}.
     */
    public final void onStop(HACPlayer player, @NotNull Object packet) {
        this.onStop(player, this.wrappedClass.cast(packet));
    }

    /**
     * Called when {@link AbstractPacketEventExecutor#update(HACPlayer, WrappedPacket)} returns false.
     *
     * @param player the player
     * @param packet the {@link WrappedPacket}.
     */
    protected abstract void onStop(HACPlayer player, @NotNull T packet);

    /**
     * Gets wrapped class.
     *
     * @return the wrapped class
     */
    public Class<T> getWrappedClass() {
        return this.wrappedClass;
    }

    /**
     * Gets priority.
     *
     * @return the priority
     */
    public Priority getPriority() {
        return this.priority;
    }
}
