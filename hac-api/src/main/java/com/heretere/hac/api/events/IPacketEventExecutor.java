package com.heretere.hac.api.events;

import com.google.common.reflect.TypeToken;
import com.heretere.hac.api.events.packets.wrapper.WrappedPacket;

public abstract class IPacketEventExecutor<T extends WrappedPacket> extends TypeToken<T> {
}
