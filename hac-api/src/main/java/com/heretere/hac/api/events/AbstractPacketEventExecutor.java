package com.heretere.hac.api.events;

import com.heretere.hac.api.events.packets.PacketReferences;
import com.heretere.hac.api.events.packets.wrapper.WrappedPacket;
import com.heretere.hac.api.player.HACPlayer;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractPacketEventExecutor<T extends WrappedPacket> {
    private final Priority priority;
    private final String identifier;
    private final Class<T> wrappedClass;

    protected AbstractPacketEventExecutor(@NotNull Priority priority, @NotNull String identifier, @NotNull PacketReferences.PacketReference<T> reference) {
        this.priority = priority;
        this.identifier = identifier + "_" + reference.getIdentifier();
        this.wrappedClass = reference.getWrappedPacketClass();
    }

    public boolean execute(@NotNull HACPlayer player, @NotNull Object packet) {
        return this.execute(player, this.wrappedClass.cast(packet));
    }

    public void onStop(@NotNull HACPlayer player, @NotNull Object packet) {
        this.onStop(player, this.wrappedClass.cast(packet));
    }

    protected abstract boolean execute(@NotNull HACPlayer player, @NotNull T packet);

    protected abstract void onStop(@NotNull HACPlayer player, @NotNull T packet);

    public Priority getPriority() {
        return this.priority;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public Class<T> getWrappedClass() {
        return this.wrappedClass;
    }
}
