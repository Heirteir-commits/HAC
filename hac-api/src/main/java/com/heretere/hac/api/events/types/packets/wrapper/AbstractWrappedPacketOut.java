package com.heretere.hac.api.events.types.packets.wrapper;


import com.heretere.hac.api.events.types.packets.PacketConstants;

/**
 * The type Abstract wrapped packet out.
 */
public abstract class AbstractWrappedPacketOut implements WrappedPacket {
    private final PacketConstants.Out type;

    private int entityId;

    /**
     * Instantiates a new Abstract wrapped packet out.
     *
     * @param type the type
     */
    public AbstractWrappedPacketOut(PacketConstants.Out type) {
        this.type = type;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public PacketConstants.Out getType() {
        return this.type;
    }

    /**
     * Gets entity id.
     *
     * @return the entity id
     */
    public int getEntityId() {
        return this.entityId;
    }
}
