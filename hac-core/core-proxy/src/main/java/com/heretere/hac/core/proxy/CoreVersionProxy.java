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

    /**
     * Base load.
     */
    public final void baseLoad() {
        this.registerPackets();
        this.load();
    }

    /**
     * Base unload.
     */
    public final void baseUnload() {
        this.unload();
    }

    protected abstract void load();

    protected abstract void unload();
}
