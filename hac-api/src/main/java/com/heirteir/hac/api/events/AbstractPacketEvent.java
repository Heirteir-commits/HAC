package com.heirteir.hac.api.events;

import com.heirteir.hac.api.events.packets.wrapper.WrappedPacket;
import com.heirteir.hac.api.player.HACPlayer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public abstract class AbstractPacketEvent<T extends WrappedPacket> {
    private final Class<T> wrappedClass;
    private final Priority priority;

    public final boolean update(HACPlayer player, @NotNull Object packet) {
        return this.update(player, this.wrappedClass.cast(packet));
    }

    protected abstract boolean update(HACPlayer player, @NotNull T packet);

    public final void onStop(HACPlayer player, @NotNull Object packet) {
        this.onStop(player, this.wrappedClass.cast(packet));
    }

    protected abstract void onStop(HACPlayer player, @NotNull T packet);

    public enum Priority {
        PRE_PROCESS,
        PROCESS_1,
        PROCESS_2,
        PROCESS_3,
        PROCESS_4
    }
}
