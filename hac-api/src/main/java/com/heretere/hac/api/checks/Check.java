package com.heretere.hac.api.checks;

import com.heretere.hac.api.events.PacketEventExecutor;
import com.heretere.hac.api.events.Priority;
import com.heretere.hac.api.events.packets.PacketReferences;
import com.heretere.hac.api.events.packets.wrapper.WrappedPacket;
import com.heretere.hac.api.player.HACPlayer;
import org.jetbrains.annotations.NotNull;

public abstract class Check<T extends WrappedPacket> extends PacketEventExecutor<T> {
    /**
     * Base identifier I used for sorting the execution order. This value is appended onto the parent identifier.
     */
    private final @NotNull String baseIdentifier;

    protected Check(
        final @NotNull Priority priority,
        final @NotNull String identifier,
        final @NotNull PacketReferences.PacketReference<T> reference
    ) {
        super(priority, identifier, reference);
        this.baseIdentifier = identifier;
    }

    @Override
    public final boolean execute(
        final @NotNull HACPlayer player,
        final @NotNull T packet
    ) {
        return !this.isEnabled() || this.check(player, packet);
    }

    protected abstract boolean check(
        @NotNull HACPlayer player,
        @NotNull T packet
    );

    @Override
    public abstract void onStop(
        @NotNull HACPlayer player,
        @NotNull T packet
    );

    /**
     * Gets the base identifier value of this check.
     *
     * @return The value of the base identifier.
     */
    public @NotNull String getBaseIdentifier() {
        return this.baseIdentifier;
    }

    /**
     * Whether or not the check is currently enabled.
     *
     * @return true if enabled
     */
    public abstract boolean isEnabled();

    /**
     * Set whether or not the check is enabled.
     *
     * @param enabled true if enabled
     */
    public abstract void setEnabled(boolean enabled);
}
