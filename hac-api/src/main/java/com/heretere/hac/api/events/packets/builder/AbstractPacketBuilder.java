package com.heretere.hac.api.events.packets.builder;

import com.google.common.collect.Sets;
import com.heretere.hac.api.events.packets.wrapper.WrappedPacket;
import com.heretere.hac.api.player.HACPlayer;

import java.util.Set;

public abstract class AbstractPacketBuilder<T extends WrappedPacket> {
    private final Set<Class<?>> nmsClasses;

    protected AbstractPacketBuilder(Class<?> base, Class<?>... nmsClasses) {
        this.nmsClasses = Sets.newIdentityHashSet(); //since were only storing classes identityHashSet is beneficial here for speed
        this.nmsClasses.add(base);
        this.nmsClasses.addAll(Sets.newHashSet(nmsClasses));
    }

    public abstract T create(HACPlayer player, Object packet);

    public abstract Class<T> getWrappedClass();

    public final Set<Class<?>> getPacketClasses() {
        return this.nmsClasses;
    }
}
