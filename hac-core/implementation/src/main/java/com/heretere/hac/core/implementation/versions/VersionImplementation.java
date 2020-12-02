package com.heretere.hac.core.implementation.versions;

import com.heretere.hac.core.implementation.packets.channel.ChannelInjectorBase;

public interface VersionImplementation {
    void registerPackets();

    ChannelInjectorBase getChannelInjector();
}
