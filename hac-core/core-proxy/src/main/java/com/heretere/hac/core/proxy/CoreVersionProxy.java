package com.heretere.hac.core.proxy;

import com.heretere.hac.api.HACAPI;
import com.heretere.hac.core.proxy.packets.channel.AbstractChannelInjector;
import com.heretere.hac.core.proxy.player.HACPlayerListUpdater;
import com.heretere.hac.core.proxy.player.data.player.PlayerData;
import com.heretere.hac.core.proxy.player.data.player.PlayerDataFactory;
import com.heretere.hac.util.plugin.AbstractHACPlugin;
import com.heretere.hac.util.proxy.AbstractVersionProxy;
import org.jetbrains.annotations.NotNull;

/**
 * The type Core version proxy.
 */
public abstract class CoreVersionProxy implements AbstractVersionProxy {
    /**
     * The parent HAC plugin for this proxy.
     */
    private final AbstractHACPlugin parent;
    /**
     * The class responsible for updating the HACPlayerList in the api.
     */
    private final HACPlayerListUpdater hacPlayerListUpdater;
    /**
     * The factory responsible for creating new PlayerData instances for each HACPlayer.
     */
    private final PlayerDataFactory playerDataFactory;

    /**
     * Instantiates a new Core version proxy.
     *
     * @param parent the parent
     */
    protected CoreVersionProxy(@NotNull final AbstractHACPlugin parent) {
        this.parent = parent;
        this.hacPlayerListUpdater = new HACPlayerListUpdater(parent, this);
        this.playerDataFactory = new PlayerDataFactory(HACAPI.getInstance());
    }

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
        HACAPI.getInstance().getErrorHandler().setHandler(ex -> this.parent.getLog().severe(ex));

        this.registerPackets();
        this.parent.getLog().info(() -> "Registering player data builder.");
        HACAPI.getInstance().getHacPlayerList().getBuilder().registerDataBuilder(PlayerData.class, playerDataFactory);

        this.hacPlayerListUpdater.load();

        this.load();
    }

    /**
     * Base unload.
     */
    public final void baseUnload() {
        this.hacPlayerListUpdater.unload();

        this.parent.getLog().info(() -> "Unregistering player data builder.");
        HACAPI.getInstance().getHacPlayerList().getBuilder().unregisterDataBuilder(PlayerData.class);

        this.unload();
        HACAPI.getInstance().unload();
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
