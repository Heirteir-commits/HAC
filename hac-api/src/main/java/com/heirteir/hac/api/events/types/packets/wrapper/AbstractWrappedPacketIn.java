package com.heirteir.hac.api.events.types.packets.wrapper;

import com.heirteir.hac.api.events.types.packets.PacketConstants;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public abstract class AbstractWrappedPacketIn implements WrappedPacket {
    private final PacketConstants.In type;
}
