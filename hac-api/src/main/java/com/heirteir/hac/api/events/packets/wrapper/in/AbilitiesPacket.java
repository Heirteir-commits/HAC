package com.heirteir.hac.api.events.packets.wrapper.in;

import com.heirteir.hac.api.events.packets.Packet;
import com.heirteir.hac.api.events.packets.wrapper.AbstractWrappedPacketIn;
import lombok.Getter;

//PacketPlayInAbilities
@Getter
public final class AbilitiesPacket extends AbstractWrappedPacketIn {

    private boolean flying;

    public AbilitiesPacket() {
        super(Packet.In.ABILITIES);
    }
}
