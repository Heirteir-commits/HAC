package com.heirteir.hac.api.events.packets.wrapper;

import com.heirteir.hac.api.events.packets.Packet;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public abstract class AbstractWrappedPacketIn implements WrappedPacket {
    private final Packet.In type;
}
