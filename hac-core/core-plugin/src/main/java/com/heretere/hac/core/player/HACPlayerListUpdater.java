/*
 * MIT License
 *
 * Copyright (c) 2021 Justin Heflin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

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
 * This is responsible for populating and removing HACPlayer's instances from the hac-api HACPlayerList.
 * Normally this is only accessed at startup/shutdown to provide a way of tracking player's that have
 * an attach HACPlayer instance.
 */
public final class HACPlayerListUpdater implements Listener {
    /**
     * The HACAPI reference.
     */
    private final @NotNull HACAPI api;
    /**
     * The parent plugin reference.
     */
    private final @NotNull Core core;

    /**
     * Creates a new instance.
     *
     * @param api  the HACAPI reference
     * @param core the parent
     */
    public HACPlayerListUpdater(
        final @NotNull HACAPI api,
        final @NotNull Core core
    ) {
        this.api = api;
        this.core = core;
    }

    /**
     * Registers the events in this class, and attaches a HACPlayer to all online players.
     */
    public void load() {
        Bukkit.getPluginManager().registerEvents(this, this.core);
        Bukkit.getOnlinePlayers().forEach(this::addPlayer);
    }

    /**
     * Unregisters the events, and removes the HACPlayer from all online players.
     */
    public void unload() {
        HandlerList.unregisterAll(this);
        Bukkit.getOnlinePlayers().forEach(this::removePlayer);
    }

    private void addPlayer(final @NotNull Player player) {
        this.core.getProxy().getChannelInjector().inject(this.api.getHacPlayerList().getPlayer(player));
    }

    private void removePlayer(final @NotNull Player player) {
        this.api
            .getHacPlayerList()
            .removePlayer(player)
            .ifPresent(hacPlayer -> this.core.getProxy().getChannelInjector().remove(hacPlayer));
    }

    /**
     * This is ran when the player joins the server.
     *
     * @param e The player join event
     */
    @EventHandler
    public void onPlayerJoin(final @NotNull PlayerJoinEvent e) {
        this.addPlayer(e.getPlayer());
    }

    /**
     * This is ran when a player disconnects from the server.
     *
     * @param e the player quit event
     */
    @EventHandler
    public void onPlayerQuit(final @NotNull PlayerQuitEvent e) {
        this.removePlayer(e.getPlayer());
    }

}
