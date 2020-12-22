package com.heretere.hac.core.proxy.versions.sixteen.packets.builder.serverside;

import com.heretere.hac.api.events.packets.factory.AbstractPacketFactory;
import com.heretere.hac.api.events.packets.wrapper.serverside.EntityVelocityPacket;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.util.plugin.AbstractHACPlugin;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityVelocity;
import org.apache.commons.lang.reflect.FieldUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

/**
 * The type Entity velocity packet factory.
 */
public final class EntityVelocityPacketFactory extends AbstractPacketFactory<EntityVelocityPacket> {
    /**
     * The HACPlugin reference.
     */
    private final AbstractHACPlugin parent;
    /* Have to use reflections since fields are private and they're no getters :( */

    /**
     * The field that represents the id of the entity.
     */
    private final Field id;
    /**
     * The field that represents the dx of the packet.
     */
    private final Field x;
    /**
     * The field that represents the dy of the packet.
     */
    private final Field y;
    /**
     * The field that represents the dz of the packet.
     */
    private final Field z;

    /**
     * Instantiates a new Entity velocity packet factory.
     *
     * @param parent the parent
     */
    public EntityVelocityPacketFactory(@NotNull final AbstractHACPlugin parent) {
        super(PacketPlayOutEntityVelocity.class);
        this.parent = parent;

        this.id = FieldUtils.getDeclaredField(PacketPlayOutEntityVelocity.class, "a", true);
        this.x = FieldUtils.getDeclaredField(PacketPlayOutEntityVelocity.class, "b", true);
        this.y = FieldUtils.getDeclaredField(PacketPlayOutEntityVelocity.class, "c", true);
        this.z = FieldUtils.getDeclaredField(PacketPlayOutEntityVelocity.class, "d", true);
    }

    @Override
    public EntityVelocityPacket create(
            @NotNull final HACPlayer player,
            @NotNull final Object packet
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
            output = null;

            this.parent.getLog().severe(e);
        }

        return output;
    }

    @Override
    public Class<EntityVelocityPacket> getWrappedClass() {
        return EntityVelocityPacket.class;
    }
}
