package com.heretere.hac.api.checks;

import com.heretere.hac.api.events.AbstractPacketEventExecutor;
import com.heretere.hac.api.events.Priority;
import com.heretere.hac.api.events.packets.PacketReferences;
import com.heretere.hac.api.events.packets.wrapper.WrappedPacket;
import com.heretere.hac.api.player.HACPlayer;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractCheck<T extends WrappedPacket> extends AbstractPacketEventExecutor<T> {
    private final String baseIdentifier;

    protected AbstractCheck(@NotNull Priority priority, @NotNull String identifier, @NotNull PacketReferences.PacketReference<T> reference) {
        super(priority, identifier, reference);
        this.baseIdentifier = identifier;
    }

    @Override
    public final boolean execute(@NotNull HACPlayer player, @NotNull T packet) {
        return !this.isEnabled() || this.check(player, packet);
    }

    protected abstract boolean check(@NotNull HACPlayer player, @NotNull T packet);

    @Override
    public abstract void onStop(@NotNull HACPlayer player, @NotNull T packet);

    public String getBaseIdentifier() {
        return baseIdentifier;
    }

    public abstract boolean isEnabled();

    public abstract void setEnabled(boolean enabled);
}
