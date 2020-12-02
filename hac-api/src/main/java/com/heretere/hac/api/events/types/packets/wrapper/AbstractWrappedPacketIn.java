package com.heretere.hac.api.events.types.packets.wrapper;


import com.heretere.hac.api.events.types.packets.PacketConstants;

/**
 * The type Abstract wrapped packet in.
 */
public abstract class AbstractWrappedPacketIn implements WrappedPacket {
    private final PacketConstants.In type;

    /**
     * Instantiates a new Abstract wrapped packet in.
     *
     * @param type the type
     */
    public AbstractWrappedPacketIn(PacketConstants.In type) {
        this.type = type;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public PacketConstants.In getType() {
        return this.type;
    }
}
