package com.heretere.hac.core.proxy;

import com.heretere.hac.api.HACAPI;
import com.heretere.hac.core.proxy.packets.channel.AbstractChannelInjector;
import com.heretere.hac.core.proxy.player.HACPlayerListUpdater;
import com.heretere.hac.core.proxy.player.data.player.PlayerData;
import com.heretere.hac.core.proxy.player.data.player.PlayerDataBuilder;
import com.heretere.hac.util.plugin.AbstractHACPlugin;
import com.heretere.hac.util.proxy.AbstractVersionProxy;

public abstract class CoreVersionProxy implements AbstractVersionProxy {
    private final AbstractHACPlugin parent;
    private final HACPlayerListUpdater hacPlayerListUpdater;
    private final PlayerDataBuilder playerDataBuilder;

    protected CoreVersionProxy(AbstractHACPlugin parent) {
        this.parent = parent;
        this.hacPlayerListUpdater = new HACPlayerListUpdater(parent, this);
        this.playerDataBuilder = new PlayerDataBuilder();
    }

    protected abstract void registerPackets();

    public abstract AbstractChannelInjector getChannelInjector();

    public final void baseLoad() {
        HACAPI.getInstance().getErrorHandler().setHandler(ex -> this.parent.getLog().severe(ex));

        this.registerPackets();
        this.parent.getLogger().info(() -> "Registering PlayerData");
        HACAPI.getInstance().getHacPlayerList().getBuilder().registerDataBuilder(PlayerData.class, playerDataBuilder);

        this.hacPlayerListUpdater.load();
    }

    public final void baseUnload() {
        this.hacPlayerListUpdater.unload();

        this.parent.getLog().info(() -> "Unregistering PlayerData");
        HACAPI.getInstance().getHacPlayerList().getBuilder().unregisterDataBuilder(PlayerData.class);

        this.unload();
        HACAPI.getInstance().unload();
    }

    protected abstract void load();

    protected abstract void unload();
}
