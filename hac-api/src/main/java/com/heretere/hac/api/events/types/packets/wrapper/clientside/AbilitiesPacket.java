package com.heretere.hac.api.events.types.packets.wrapper.clientside;


import com.heretere.hac.api.events.types.packets.wrapper.AbstractWrappedPacketIn;

/**
 * This is a wrapped version of the PacketPlayInAbilitiesPacket.
 */
public final class AbilitiesPacket extends AbstractWrappedPacketIn {
    private final boolean flying;


    /**
     * Instantiates a new Abilities packet.
     *
     * @param flying the flying
     */
    public AbilitiesPacket(boolean flying) {
        this.flying = flying;
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
