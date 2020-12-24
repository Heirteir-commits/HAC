package com.heretere.hac.movement.proxy;

import com.heretere.hac.util.proxy.VersionProxy;

public abstract class MovementVersionProxy implements VersionProxy {
    protected MovementVersionProxy() {
    }

    /**
     * Loads proxy plugin logic used by all version proxies, then delegates the rest of the loading to the version
     * proxy.
     */
    public final void baseLoad() {
        this.load();
    }

    /**
     * Unloads proxy plugin logic used by all version proxies, and delegates unloading to the version proxy.
     */
    public final void baseUnload() {
        this.unload();
    }

    protected abstract void load();

    protected abstract void unload();
}
