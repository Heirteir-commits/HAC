package com.heretere.hac.core.player;

import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.player.HACPlayer;
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
    private final Core core;

    /**
     * Instantiates a new Hac player list updater.
     *
     * @param core the parent
     */
    public HACPlayerListUpdater(@NotNull final Core core) {
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

    private void addPlayer(@NotNull final Player player) {
        this.core.getProxy().getChannelInjector().inject(HACAPI.getInstance().getHacPlayerList().getPlayer(player));
    }

    private void removePlayer(@NotNull final Player player) {
        HACPlayer hacPlayer = HACAPI.getInstance().getHacPlayerList().removePlayer(player);

        this.core.getProxy().getChannelInjector().remove(hacPlayer);
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
