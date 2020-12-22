package com.heretere.hac.api.events;

import com.heretere.hac.api.events.packets.PacketReferences;
import com.heretere.hac.api.events.packets.wrapper.WrappedPacket;
import com.heretere.hac.api.player.HACPlayer;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractPacketEventExecutor<T extends WrappedPacket> {
    /**
     * The run priority of the event executor.
     */
    private final Priority priority;
    /**
     * The unique identifier for this event executor.
     */
    private final String identifier;

    /**
     * The WrappedPacket class this executor handles.
     */
    private final Class<T> wrappedClass;

    protected AbstractPacketEventExecutor(
        @NotNull final Priority priority,
        @NotNull final String identifier,
        @NotNull final PacketReferences.PacketReference<T> reference
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
        @NotNull final HACPlayer player,
        @NotNull final Object packet
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
        @NotNull final HACPlayer player,
        @NotNull final Object packet
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
    public Priority getPriority() {
        return this.priority;
    }

    /**
     * The unique identifier for the event executor.
     *
     * @return The unique identifier
     */
    public String getIdentifier() {
        return this.identifier;
    }

    /**
     * The wrapped packet class this executor handles.
     *
     * @return Wrapped packet class
     */
    public Class<T> getWrappedClass() {
        return this.wrappedClass;
    }
}
