package com.heretere.hac.api.events.packets.wrapper.clientside;


import com.heretere.hac.api.events.packets.wrapper.WrappedPacketIn;

/**
 * This is a wrapped version of the PacketPlayInAbilitiesPacket.
 */
public final class AbilitiesPacket implements WrappedPacketIn {
    /**
     * Represents if the player is currently flying or not.
     */
    private final boolean flying;


    /**
     * Instantiates a new Abilities packet.
     *
     * @param flying the flying
     */
    public AbilitiesPacket(final boolean flying) {
        this.flying = flying;
    }

    /**
     * Whether the packet says the player is flying or not.
     *
     * @return Whether they are flying or not.
     */
    public boolean isFlying() {
        return this.flying;
    }
}
