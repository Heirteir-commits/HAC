package com.heirteir.hac.core.packets.builder;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.heirteir.hac.api.API;
import com.heirteir.hac.api.events.packets.wrapper.WrappedPacket;
import com.heirteir.hac.api.events.packets.wrapper.in.AbilitiesPacket;
import com.heirteir.hac.api.events.packets.wrapper.in.EntityActionPacket;
import com.heirteir.hac.api.events.packets.wrapper.in.FlyingPacket;
import com.heirteir.hac.api.events.packets.wrapper.out.EntityVelocityPacket;
import com.heirteir.hac.api.util.reflections.version.ServerVersion;
import com.heirteir.hac.core.Core;
import com.heirteir.hac.core.packets.builder.conversion.ConvertType;

import java.util.AbstractMap;
import java.util.Map;

public class PacketBuilders {
    private final Core core;
    private final Map<Class<? extends WrappedPacket>, WrappedPacketBuilder> builders;

    public PacketBuilders(Core core) {
        this.core = core;
        this.builders = Maps.newHashMap();

        this.create("PacketPlayInFlying", FlyingPacket.class)
                .addField("x", "x", ConvertType.DOUBLE, null)
                .addField("y", "y", ConvertType.DOUBLE, null)
                .addField("z", "z", ConvertType.DOUBLE, null)
                .addField("yaw", "yaw", ConvertType.FLOAT, null)
                .addField("pitch", "pitch", ConvertType.FLOAT, null)
                .addField("hasPos", "hasPos", ConvertType.NONE, null)
                .addField("hasLook", "hasLook", ConvertType.NONE, null)
                .addField("f", "onGround", ConvertType.NONE, null);

        this.create("PacketPlayInAbilities", AbilitiesPacket.class)
                .setValid(API.INSTANCE.getReflections().getVersion().lessThanOrEqual(ServerVersion.FIFTEEN_R1))
                .addField("b", "flying", ConvertType.NONE, null)
                .setValid(API.INSTANCE.getReflections().getVersion().greaterThanOrEqual(ServerVersion.SIXTEEN_R1))
                .addField("a", "flying", ConvertType.NONE, null);

        this.create("PacketPlayInEntityAction", EntityActionPacket.class)
                .addField("animation", "action", ConvertType.NONE, Lists.newArrayList(
                        new AbstractMap.SimpleImmutableEntry<>(Enum.class, animation -> EntityActionPacket.Action.fromString(animation.name()))
                ));

        this.create("PacketPlayOutEntityVelocity", EntityVelocityPacket.class)
                .addField("a", "entityId", ConvertType.INTEGER, null)
                .addField("b", "x", ConvertType.DOUBLE, Lists.newArrayList(
                        new AbstractMap.SimpleImmutableEntry<>(Double.class, x -> x / EntityVelocityPacket.CONVERSION)
                ))
                .addField("c", "y", ConvertType.DOUBLE, Lists.newArrayList(
                        new AbstractMap.SimpleImmutableEntry<>(Double.class, y -> y / EntityVelocityPacket.CONVERSION)
                ))
                .addField("d", "z", ConvertType.DOUBLE, Lists.newArrayList(
                        new AbstractMap.SimpleImmutableEntry<>(Double.class, z -> z / EntityVelocityPacket.CONVERSION)
                ));
    }

    private WrappedPacketBuilder create(String nmsClass, Class<? extends WrappedPacket> type) {
        WrappedPacketBuilder builder = new WrappedPacketBuilder(this.core, nmsClass, type);
        this.builders.put(type, builder);
        return builder;
    }

    public WrappedPacketBuilder get(Class<? extends WrappedPacket> type) {
        return this.builders.get(type);
    }
}
