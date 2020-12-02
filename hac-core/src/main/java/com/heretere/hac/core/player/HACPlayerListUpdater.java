package com.heretere.hac.core.player;

import com.heretere.hac.api.API;
import com.heretere.hac.core.Core;
import com.heretere.hac.core.packets.ChannelInjector;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public final class HACPlayerListUpdater implements Listener {
    private final ChannelInjector channelInjector;

    public HACPlayerListUpdater(Core core) {
        this.channelInjector = core.getChannelInjector();
        this.load(core);
    }

    public void load(Core core) {
        Bukkit.getPluginManager().registerEvents(this, core);
        Bukkit.getOnlinePlayers().forEach(this::addPlayer);
    }

    public void unload() {
        HandlerList.unregisterAll(this);
        Bukkit.getOnlinePlayers().forEach(this::removePlayer);
    }

    private void addPlayer(Player player) {
        this.channelInjector.inject(API.INSTANCE.getHacPlayerList().getPlayer(player));
    }

    private void removePlayer(Player player) {
        this.channelInjector.remove(player);
        API.INSTANCE.getHacPlayerList().removePlayer(player);
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
