package com.heretere.hac.api.events.packets.builder;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.heretere.hac.api.events.packets.wrapper.WrappedPacket;
import com.heretere.hac.api.player.HACPlayer;

import java.util.Set;

public abstract class AbstractPacketBuilder<T extends WrappedPacket> {
    private final ImmutableSet<Class<?>> nmsClasses;

    protected AbstractPacketBuilder(Class<?> base, Class<?>... nmsClasses) {
        Set<Class<?>> classes = Sets.newHashSet(nmsClasses);
        classes.add(base);
        this.nmsClasses = ImmutableSet.copyOf(classes);
    }

    public abstract T create(HACPlayer player, Object packet);

    public abstract Class<T> getWrappedClass();

    public final ImmutableSet<Class<?>> getPacketClasses() {
        return this.nmsClasses;
    }
}
