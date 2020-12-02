package com.heirteir.hac.core.packets.builder.conversion;

import com.google.common.collect.Sets;
import com.heirteir.hac.api.util.reflections.types.WrappedField;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractMap;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public final class ConvertInfo<T, K> {

    private final ConvertType convertType;
    private final WrappedField nmsField;
    private final WrappedField wrappedPacketField;
    private final Set<AbstractMap.SimpleImmutableEntry<Class<T>, Function<T, K>>> computations;

    public ConvertInfo(ConvertType convertType, WrappedField nmsField, WrappedField wrappedPacketField, @NotNull List<AbstractMap.SimpleImmutableEntry<Class<T>, Function<T, K>>> computations) {
        this.convertType = convertType;
        this.nmsField = nmsField;
        this.wrappedPacketField = wrappedPacketField;
        this.computations = Sets.newLinkedHashSet(computations);
    }

    public void define(Object wrappedPacket, Object packet) throws IllegalAccessException, NoSuchFieldException {
        Object value = this.nmsField.get(Object.class, packet);
        switch (this.convertType) {
            case FLOAT:
                value = ((Number) value).floatValue();
                break;
            case DOUBLE:
                value = ((Number) value).doubleValue();
                break;
            case INTEGER:
                value = ((Number) value).intValue();
                break;
            case BYTE:
                value = ((Number) value).byteValue();
                break;
            case SHORT:
                value = ((Number) value).shortValue();
                break;
            default:
                break;
        }

        for (AbstractMap.SimpleImmutableEntry<Class<T>, Function<T, K>> computation : computations) {
            value = computation.getValue().apply(computation.getKey().cast(value));
        }

        this.wrappedPacketField.set(wrappedPacket, value);
    }
}
