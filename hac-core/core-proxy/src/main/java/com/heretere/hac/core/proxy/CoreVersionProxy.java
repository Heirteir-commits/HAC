package com.heretere.hac.core.proxy;

import com.heretere.hac.core.proxy.packets.channel.ChannelInjector;
import com.heretere.hac.util.proxy.VersionProxy;
import org.jetbrains.annotations.NotNull;

/**
 * The type Core version proxy.
 */
public abstract class CoreVersionProxy implements VersionProxy {

    /**
     * Instantiates a new Core version proxy.
     */
    protected CoreVersionProxy() { }

    /**
     * This registers the packet factories for each core-nms version.
     */
    protected abstract void registerPackets();

    /**
     * Gets channel injector.
     *
     * @return the channel injector
     */
    public abstract @NotNull ChannelInjector getChannelInjector();

    @Override public final void preLoad() {
        this.registerPackets();
        this.load();
    }

    @Override public final void preUnload() {
        this.unload();
    }

    @Override public abstract void load();

    @Override public abstract void unload();
}
