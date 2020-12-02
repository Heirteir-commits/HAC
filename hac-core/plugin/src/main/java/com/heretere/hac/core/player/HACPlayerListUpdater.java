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

public class HACPlayerListUpdater implements Listener {
    private final Core core;

    public HACPlayerListUpdater(Core core) {
        this.core = core;

        Bukkit.getPluginManager().registerEvents(this, this.core);
        Bukkit.getOnlinePlayers().forEach(this::addPlayer);
    }

    public void unload() {
        HandlerList.unregisterAll(this);
        Bukkit.getOnlinePlayers().forEach(this::removePlayer);
    }

    private void addPlayer(Player player) {
        this.core.getVersionImplementation().getChannelInjector()
                .inject(HACAPI.getInstance().getHacPlayerList().getPlayer(player));
    }

    private void removePlayer(Player player) {
        HACPlayer hacPlayer = HACAPI.getInstance().getHacPlayerList().removePlayer(player);

        this.core.getVersionImplementation().getChannelInjector()
                .remove(hacPlayer);
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
