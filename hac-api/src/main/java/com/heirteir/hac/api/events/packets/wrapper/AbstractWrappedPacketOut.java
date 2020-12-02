package com.heirteir.hac.api.events.packets.wrapper;

import com.heirteir.hac.api.events.packets.PacketConstants;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public abstract class AbstractWrappedPacketOut implements WrappedPacket {
    private final PacketConstants.Out type;

    private int entityId;
}
