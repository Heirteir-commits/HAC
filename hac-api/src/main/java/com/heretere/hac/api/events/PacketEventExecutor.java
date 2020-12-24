package com.heretere.hac.api.events;

import com.heretere.hac.api.events.packets.PacketReferences;
import com.heretere.hac.api.events.packets.wrapper.WrappedPacket;
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

    protected PacketEventExecutor(
        final @NotNull Priority priority,
        final @NotNull String identifier,
        final @NotNull PacketReferences.PacketReference<T> reference
    ) {
        this.priority = priority;
        this.identifier = identifier + "_" + reference.getIdentifier();
        this.wrappedClass = reference.getWrappedPacketClass();
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
}
