package com.heretere.hac.core.proxy.player;

import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.core.proxy.VersionProxy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public final class HACPlayerListUpdater implements Listener {
    private final Plugin parent;
    private final VersionProxy proxy;

    public HACPlayerListUpdater(Plugin parent, VersionProxy proxy) {
        this.parent = parent;
        this.proxy = proxy;
    }

    public void load() {
        Bukkit.getPluginManager().registerEvents(this, parent);
        Bukkit.getOnlinePlayers().forEach(this::addPlayer);
    }

    public void unload() {
        HandlerList.unregisterAll(this);
        Bukkit.getOnlinePlayers().forEach(this::removePlayer);
    }

    private void addPlayer(Player player) {
        this.proxy.getChannelInjector().inject(HACAPI.getInstance().getHacPlayerList().getPlayer(player));
    }

    private void removePlayer(Player player) {
        HACPlayer hacPlayer = HACAPI.getInstance().getHacPlayerList().removePlayer(player);

        this.proxy.getChannelInjector().remove(hacPlayer);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        this.addPlayer(e.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        this.removePlayer(e.getPlayer());
    }

}
