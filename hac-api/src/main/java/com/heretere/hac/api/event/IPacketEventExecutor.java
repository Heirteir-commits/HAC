package com.heretere.hac.api.event;

import com.google.common.reflect.TypeToken;
import com.heretere.hac.api.event.packet.wrapper.WrappedPacket;

public abstract class IPacketEventExecutor<T extends WrappedPacket> extends TypeToken<T> {
}
