package com.heirteir.hac.api.events.packets.wrapper.in;

import com.heirteir.hac.api.events.packets.PacketConstants;
import com.heirteir.hac.api.events.packets.wrapper.AbstractWrappedPacketIn;
import lombok.Getter;

//PacketPlayInAbilities
@Getter
public final class AbilitiesPacket extends AbstractWrappedPacketIn {

    private boolean flying;

    public AbilitiesPacket() {
        super(PacketConstants.In.ABILITIES);
    }

    public AbilitiesPacket(boolean flying) {
        this();
        this.flying = flying;
    }
}
