package com.heretere.hac.api.events.packets.factory;

import com.google.common.collect.Sets;
import com.heretere.hac.api.events.packets.wrapper.WrappedPacket;
import com.heretere.hac.api.player.HACPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public abstract class AbstractPacketFactory<T extends WrappedPacket> {
    /**
     * The nms packet class that this factory handles.
     */
    private final Set<Class<?>> nmsClasses;

    protected AbstractPacketFactory(@NotNull final Class<?> base, @NotNull final Class<?>... nmsClasses) {
        //since were only storing classes identityHashSet is beneficial here for speed
        this.nmsClasses = Sets.newIdentityHashSet();
        this.nmsClasses.add(base);
        this.nmsClasses.addAll(Sets.newHashSet(nmsClasses));
    }

    /**
     * Creates a T wrapped packet instance based on the passed in NMS packet.
     *
     * @param player The HACPlayer
     * @param packet The NMS packet
     * @return A T wrapped packet
     */
    public abstract T create(HACPlayer player, Object packet);

    /**
     * The T wrapped class this factory creates.
     *
     * @return T wrapped class
     */
    public abstract Class<T> getWrappedClass();

    /**
     * Passes the IdentityHashSet to check if this factory handles a specific class.
     *
     * @return The IdentityHashSet
     */
    public final Set<Class<?>> getPacketClasses() {
        return this.nmsClasses;
    }
}
