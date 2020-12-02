package com.heretere.hac.core.proxy;

import com.heretere.hac.api.HACAPI;
import com.heretere.hac.core.proxy.packets.channel.ChannelInjectorBase;
import com.heretere.hac.core.proxy.player.HACPlayerListUpdater;
import org.bukkit.plugin.Plugin;

public abstract class VersionProxy {
    private final HACPlayerListUpdater hacPlayerListUpdater;

    protected VersionProxy(Plugin parent) {
        this.hacPlayerListUpdater = new HACPlayerListUpdater(parent, this);
    }

    protected abstract void registerPackets();

    public abstract ChannelInjectorBase getChannelInjector();

    public final void baseLoad() {
        this.registerPackets();
        this.hacPlayerListUpdater.load();
    }

    public final void baseUnload() {
        this.hacPlayerListUpdater.unload();
        this.unload();

        HACAPI.getInstance().unload();
    }

    protected abstract void load();

    protected abstract void unload();
}
