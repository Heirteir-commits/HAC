package com.heretere.hac.core.proxy;

import com.heretere.hac.core.proxy.packets.channel.AbstractChannelInjector;
import com.heretere.hac.util.proxy.AbstractVersionProxy;

/**
 * The type Core version proxy.
 */
public abstract class CoreVersionProxy implements AbstractVersionProxy {

    /**
     * Instantiates a new Core version proxy.
     */
    protected CoreVersionProxy() { }

    /**
     * Register packets.
     */
    protected abstract void registerPackets();

    /**
     * Gets channel injector.
     *
     * @return the channel injector
     */
    public abstract AbstractChannelInjector getChannelInjector();

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

    /**
     * Load.
     */
    protected abstract void load();

    /**
     * Unload.
     */
    protected abstract void unload();
}
