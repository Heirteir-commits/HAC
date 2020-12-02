package com.heirteir.hac.core.packets.builder;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.heirteir.hac.api.API;
import com.heirteir.hac.api.events.packets.wrapper.WrappedPacket;
import com.heirteir.hac.api.util.reflections.types.WrappedClass;
import com.heirteir.hac.api.util.reflections.types.WrappedConstructor;
import com.heirteir.hac.core.Core;
import com.heirteir.hac.core.packets.builder.conversion.ConvertInfo;
import com.heirteir.hac.core.packets.builder.conversion.ConvertType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.AbstractMap;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public final class WrappedPacketBuilder {

    private final Core core;
    private final WrappedClass nmsPacketClass;

    private final Class<? extends WrappedPacket> rawWrappedPacketClass;
    private final WrappedClass wrappedPacketClass;
    private final Set<ConvertInfo<?, ?>> fields;
    private boolean valid = true;
    private WrappedConstructor wrappedConstructor;

    public WrappedPacketBuilder(Core core, String nmsPacketClass, Class<? extends WrappedPacket> wrappedPacketClass) {
        this.core = core;
        this.nmsPacketClass = API.INSTANCE.getReflections().getNMSClass(nmsPacketClass);
        this.rawWrappedPacketClass = wrappedPacketClass;
        this.wrappedPacketClass = API.INSTANCE.getReflections().getClass(wrappedPacketClass);
        try {
            this.wrappedConstructor = this.wrappedPacketClass.getConstructor();
        } catch (NoSuchMethodException e) {
            this.core.getLog().reportFatalError(e);
        }
        this.fields = Sets.newHashSet();
    }

    public WrappedPacketBuilder setValid(boolean valid) {
        this.valid = valid;
        return this;
    }

    public <A, B> WrappedPacketBuilder addField(@NotNull String obfuscatedName, @NotNull String mappedName, @NotNull ConvertType convertType, @Nullable List<AbstractMap.SimpleImmutableEntry<Class<A>, Function<A, B>>> conversions) {
        if (this.valid) {
            try {
                this.fields.add(new ConvertInfo<>(convertType,
                        this.nmsPacketClass.getFieldByName(obfuscatedName),
                        this.wrappedPacketClass.getFieldByName(mappedName),
                        conversions == null ? Lists.newArrayList() : conversions));
            } catch (NoSuchFieldException e) {
                this.core.getLog().reportFatalError(e);
            }
        }
        return this;
    }

    public WrappedPacket build(Object rawPacket) {
        WrappedPacket packet;
        try {
            packet = this.wrappedConstructor.newInstance(this.rawWrappedPacketClass);
            for (ConvertInfo<?, ?> field : this.fields) {
                field.define(packet, rawPacket);
            }
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            packet = null;
            this.core.getLog().reportFatalError(e);
        }
        return packet;
    }
}
