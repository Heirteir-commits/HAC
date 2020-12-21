package com.heretere.hac.core.proxy.player;

import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.core.proxy.CoreVersionProxy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * The type Hac player list updater.
 */
public final class HACPlayerListUpdater implements Listener {
    /**
     * The parent plugin reference.
     */
    private final Plugin parent;

    /**
     * The proxy adapter parent.
     */
    private final CoreVersionProxy proxy;

    /**
     * Instantiates a new Hac player list updater.
     *
     * @param parent the parent
     * @param proxy  the proxy
     */
    public HACPlayerListUpdater(@NotNull final Plugin parent, @NotNull final CoreVersionProxy proxy) {
        this.parent = parent;
        this.proxy = proxy;
    }

    /**
     * Load.
     */
    public void load() {
        Bukkit.getPluginManager().registerEvents(this, parent);
        Bukkit.getOnlinePlayers().forEach(this::addPlayer);
    }

    /**
     * Unload.
     */
    public void unload() {
        HandlerList.unregisterAll(this);
        Bukkit.getOnlinePlayers().forEach(this::removePlayer);
    }

    private void addPlayer(@NotNull final Player player) {
        this.proxy.getChannelInjector().inject(HACAPI.getInstance().getHacPlayerList().getPlayer(player));
    }

    private void removePlayer(@NotNull final Player player) {
        HACPlayer hacPlayer = HACAPI.getInstance().getHacPlayerList().removePlayer(player);

        this.proxy.getChannelInjector().remove(hacPlayer);
    }

    /**
     * On player join.
     *
     * @param e the e
     */
    @EventHandler
    public void onPlayerJoin(@NotNull final PlayerJoinEvent e) {
        this.addPlayer(e.getPlayer());
    }

    /**
     * On player quit.
     *
     * @param e the e
     */
    @EventHandler
    public void onPlayerQuit(@NotNull final PlayerQuitEvent e) {
        this.removePlayer(e.getPlayer());
    }

}
