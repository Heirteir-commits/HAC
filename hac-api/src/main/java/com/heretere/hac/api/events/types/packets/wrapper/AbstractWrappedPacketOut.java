package com.heretere.hac.api.events.types.packets.wrapper;


/**
 * The type Abstract wrapped packet out.
 */
public abstract class AbstractWrappedPacketOut implements WrappedPacket {
    private final int entityId;


    /**
     * Instantiates a new Abstract wrapped packet out.
     *
     * @param entityId the entity id
     */
    public AbstractWrappedPacketOut(int entityId) {
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
