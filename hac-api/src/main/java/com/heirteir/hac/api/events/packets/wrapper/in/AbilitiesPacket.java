package com.heirteir.hac.api.events.packets.wrapper.in;

import com.heirteir.hac.api.events.packets.PacketConstants;
import com.heirteir.hac.api.events.packets.wrapper.AbstractWrappedPacketIn;
import lombok.Getter;

//PacketPlayInAbilities
@Getter
public final class AbilitiesPacket extends AbstractWrappedPacketIn {
    public static final AbilitiesPacket DEFAULT;

    static {
        DEFAULT = new AbilitiesPacket();
        DEFAULT.flying = false;
    }

    private boolean flying;

    public AbilitiesPacket() {
        super(PacketConstants.In.ABILITIES);
    }
}
