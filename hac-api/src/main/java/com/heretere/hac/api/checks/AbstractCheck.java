package com.heretere.hac.api.checks;

import com.heretere.hac.api.events.AbstractPacketEventExecutor;
import com.heretere.hac.api.events.Priority;
import com.heretere.hac.api.events.packets.PacketReferences;
import com.heretere.hac.api.events.packets.wrapper.WrappedPacket;
import com.heretere.hac.api.player.HACPlayer;
import org.jetbrains.annotations.NotNull;

public class AbstractCheck<T extends WrappedPacket> extends AbstractPacketEventExecutor<T> {
    protected AbstractCheck(@NotNull Priority priority, @NotNull String identifier, @NotNull PacketReferences.PacketReference<T> reference) {
        super(priority, identifier, reference);
    }

    @Override
    protected final boolean execute(@NotNull HACPlayer player, @NotNull T packet) {
        return false;
    }

    @Override
    protected final void onStop(@NotNull HACPlayer player, @NotNull T packet) {

    }
}
