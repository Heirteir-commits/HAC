package com.heretere.hac.api.events.packets.wrapper;


/**
 * The type wrapped packet out.
 */
public abstract class WrappedPacketOut implements WrappedPacket {
    /**
     * The entity id attached to this Out packet.
     */
    private final int entityId;


    /**
     * Instantiates a new wrapped packet out.
     *
     * @param entityId the entity id
     */
    protected WrappedPacketOut(final int entityId) {
        this.entityId = entityId;
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
