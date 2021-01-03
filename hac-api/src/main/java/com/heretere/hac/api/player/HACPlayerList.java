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

package com.heretere.hac.api.player;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.heretere.hac.api.HACAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Provides a way of getting any registered HACPlayer on the server.
 */
public final class HACPlayerList {

    /**
     * The HACPlayer factory instance.
     */
    private final @NotNull HACPlayerFactory factory;
    /**
     * A map of all the registered players.
     */
    private final @NotNull Map<UUID, HACPlayer> players;

    /**
     * This constructor should only ever be called by {@link HACAPI}.
     *
     * @param api    the api
     * @param parent the plugin housing the API
     */
    public HACPlayerList(
        final @NotNull HACAPI api,
        final @NotNull Plugin parent
    ) {
        this.factory = new HACPlayerFactory(api, parent, this);
        this.players = Maps.newHashMap();
    }

    /**
     * Gets a {@link HACPlayer} by {@link java.util.UUID}.
     *
     * @param uuid The UUID of the Bukkit {@link org.bukkit.entity.Player}.
     * @return An instance of {@link HACPlayer} from the player list.
     */
    public @NotNull HACPlayer getPlayer(final @NotNull UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);

        Preconditions.checkNotNull(player, String.format("No player online with uuid '%s'", uuid));

        return this.getPlayer(player);
    }

    /**
     * Gets a {@link HACPlayer} by {@link org.bukkit.entity.Player}.
     *
     * @param player The {@link org.bukkit.entity.Player} object.
     * @return An instance of {@link HACPlayer} from the player list.
     */
    public @NotNull HACPlayer getPlayer(final @NotNull Player player) {
        return this.players.computeIfAbsent(player.getUniqueId(), id -> this.factory.build(player));
    }

    /**
     * Removes a {@link HACPlayer} from the map by {@link java.util.UUID}.
     *
     * @param uuid The UUID of the Bukkit {@link org.bukkit.entity.Player}.
     * @return The removed {@link HACPlayer} instance.
     */
    public @NotNull Optional<HACPlayer> removePlayer(final @NotNull UUID uuid) {
        return Optional.ofNullable(this.players.remove(uuid));
    }

    /**
     * Removes a {@link HACPlayer} from the map by {@link org.bukkit.entity.Player}.
     *
     * @param player The {@link org.bukkit.entity.Player} object.
     * @return The removed {@link HACPlayer} instance.
     */
    public @NotNull Optional<HACPlayer> removePlayer(final @NotNull Player player) {
        return this.removePlayer(player.getUniqueId());
    }

    /**
     * Returns an {@link com.google.common.collect.ImmutableSet} copy of the current registered players.
     * This should be used strictly for looping through the current players.
     *
     * @return An {@link com.google.common.collect.ImmutableSet}
     */
    public @NotNull Set<HACPlayer> getAll() {
        return ImmutableSet.copyOf(this.players.values());
    }

    /**
     * The {@link HACPlayerFactory} instance used to create {@link HACPlayer} instances.
     *
     * @return An {@link HACPlayerFactory} instance.
     */
    public @NotNull HACPlayerFactory getFactory() {
        return this.factory;
    }
}
