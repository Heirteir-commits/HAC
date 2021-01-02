package com.heretere.hac.core.proxy.versions.sixteen.packets.builder.serverside;

import com.heretere.hac.api.event.packet.factory.PacketFactory;
import com.heretere.hac.api.event.packet.wrapper.serverside.EntityVelocityPacket;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.util.plugin.HACPlugin;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityVelocity;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

/**
 * The type Entity velocity packet factory.
 */
public final class EntityVelocityPacketFactory extends PacketFactory<EntityVelocityPacket> {
    /**
     * The HACPlugin reference.
     */
    private final @NotNull HACPlugin parent;
    /* Have to use reflections since fields are private and they're no getters :( */

    /**
     * The field that represents the id of the entity.
     */
    private final @NotNull Field id;
    /**
     * The field that represents the dx of the packet.
     */
    private final @NotNull Field x;
    /**
     * The field that represents the dy of the packet.
     */
    private final @NotNull Field y;
    /**
     * The field that represents the dz of the packet.
     */
    private final @NotNull Field z;

    /**
     * Instantiates a new Entity velocity packet factory.
     *
     * @param parent the parent
     */
    public EntityVelocityPacketFactory(final @NotNull HACPlugin parent) {
        super(PacketPlayOutEntityVelocity.class);
        this.parent = parent;

        this.id = FieldUtils.getDeclaredField(PacketPlayOutEntityVelocity.class, "a", true);
        this.x = FieldUtils.getDeclaredField(PacketPlayOutEntityVelocity.class, "b", true);
        this.y = FieldUtils.getDeclaredField(PacketPlayOutEntityVelocity.class, "c", true);
        this.z = FieldUtils.getDeclaredField(PacketPlayOutEntityVelocity.class, "d", true);
    }

    @Override
    public @NotNull EntityVelocityPacket create(
        final @NotNull HACPlayer player,
        final @NotNull Object packet
    ) {
        PacketPlayOutEntityVelocity velocity = (PacketPlayOutEntityVelocity) packet;

        EntityVelocityPacket output;
        try {
            int idValue = (int) FieldUtils.readField(this.id, velocity);
            double xValue = (int) FieldUtils.readField(this.x, velocity);
            double yValue = (int) FieldUtils.readField(this.y, velocity);
            double zValue = (int) FieldUtils.readField(this.z, velocity);

            output = new EntityVelocityPacket(
                idValue,
                xValue / EntityVelocityPacket.CONVERSION,
                yValue / EntityVelocityPacket.CONVERSION,
                zValue / EntityVelocityPacket.CONVERSION
            );
        } catch (IllegalAccessException e) {
            output = new EntityVelocityPacket(0, 0, 0, 0);

            this.parent.getLog().severe(e);
        }

        return output;
    }

    @Override
    public @NotNull Class<EntityVelocityPacket> getWrappedClass() {
        return EntityVelocityPacket.class;
    }
}
