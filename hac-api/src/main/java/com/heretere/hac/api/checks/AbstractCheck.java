package com.heretere.hac.api.checks;

import com.heretere.hac.api.events.AbstractPacketEventExecutor;
import com.heretere.hac.api.events.Priority;
import com.heretere.hac.api.events.packets.PacketReferences;
import com.heretere.hac.api.events.packets.wrapper.WrappedPacket;
import com.heretere.hac.api.player.HACPlayer;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractCheck<T extends WrappedPacket> extends AbstractPacketEventExecutor<T> {
    /**
     * Base identifier I used for sorting the execution order. This value is appended onto the parent identifier.
     */
    private final String baseIdentifier;

    protected AbstractCheck(
        @NotNull final Priority priority,
        @NotNull final String identifier,
        @NotNull final PacketReferences.PacketReference<T> reference
    ) {
        super(priority, identifier, reference);
        this.baseIdentifier = identifier;
    }

    @Override
    public final boolean execute(
        @NotNull final HACPlayer player,
        @NotNull final T packet
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
    public String getBaseIdentifier() {
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
