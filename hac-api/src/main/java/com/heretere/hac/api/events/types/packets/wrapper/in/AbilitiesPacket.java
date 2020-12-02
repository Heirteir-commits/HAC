package com.heretere.hac.api.events.types.packets.wrapper.in;

import com.heretere.hac.api.events.types.packets.PacketConstants;
import com.heretere.hac.api.events.types.packets.wrapper.AbstractWrappedPacketIn;
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
