package com.heretere.hac.api.events.types.packets.wrapper.clientside;


import com.heretere.hac.api.events.types.packets.PacketConstants;
import com.heretere.hac.api.events.types.packets.wrapper.AbstractWrappedPacketIn;

/**
 * This is a wrapped version of the PacketPlayInAbilitiesPacket.
 */
public abstract class AbilitiesPacket extends AbstractWrappedPacketIn {
    private boolean flying;

    /**
     * Instantiates a new Abilities packet.
     */
    public AbilitiesPacket() {
        super(PacketConstants.In.ABILITIES);
    }

    /**
     * Whether the packet says the player is flying or not.
     *
     * @return Whether they are flying or not.
     */
    public boolean isFlying() {
        return flying;
    }
}
