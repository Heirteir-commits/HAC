package com.heretere.hac.core.player;

import com.heretere.hac.api.HACAPI;
import com.heretere.hac.core.Core;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

/**
 * The type Hac player list updater.
 */
public final class HACPlayerListUpdater implements Listener {
    /**
     * The parent plugin reference.
     */
    private final @NotNull Core core;

    /**
     * Instantiates a new Hac player list updater.
     *
     * @param core the parent
     */
    public HACPlayerListUpdater(final @NotNull Core core) {
        this.core = core;
    }

    /**
     * Load.
     */
    public void load() {
        Bukkit.getPluginManager().registerEvents(this, this.core);
        Bukkit.getOnlinePlayers().forEach(this::addPlayer);
    }

    /**
     * Unload.
     */
    public void unload() {
        HandlerList.unregisterAll(this);
        Bukkit.getOnlinePlayers().forEach(this::removePlayer);
    }

    private void addPlayer(final @NotNull Player player) {
        this.core.getProxy().getChannelInjector().inject(HACAPI.getInstance().getHacPlayerList().getPlayer(player));
    }

    private void removePlayer(final @NotNull Player player) {
        HACAPI.getInstance()
              .getHacPlayerList()
              .removePlayer(player)
              .ifPresent(hacPlayer -> this.core.getProxy().getChannelInjector().remove(hacPlayer));
    }

    /**
     * On player join.
     *
     * @param e the e
     */
    @EventHandler
    public void onPlayerJoin(final @NotNull PlayerJoinEvent e) {
        this.addPlayer(e.getPlayer());
    }

    /**
     * On player quit.
     *
     * @param e the e
     */
    @EventHandler
    public void onPlayerQuit(final @NotNull PlayerQuitEvent e) {
        this.removePlayer(e.getPlayer());
    }

}
