package com.heretere.hac.api.player;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.heretere.hac.api.HACAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Provides a way of getting any registered HACPlayer on the server.
 */
public final class HACPlayerList {

    /**
     * The HACPlayer factory instance.
     */
    private final HACPlayerFactory builder;
    /**
     * A map of all the registered players.
     */
    private final Map<UUID, HACPlayer> players;

    /**
     * This constructor should only ever be called by {@link HACAPI}.
     *
     * @param api the api
     */
    public HACPlayerList(@NotNull final HACAPI api) {
        this.builder = new HACPlayerFactory(api, this);
        this.players = Maps.newHashMap();
    }

    /**
     * Gets a {@link HACPlayer} by {@link java.util.UUID}.
     *
     * @param uuid The UUID of the Bukkit {@link org.bukkit.entity.Player}.
     * @return An instance of {@link HACPlayer} from the player list.
     */
    public HACPlayer getPlayer(@NotNull final UUID uuid) {
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
    public HACPlayer getPlayer(@NotNull final Player player) {
        return this.players.computeIfAbsent(player.getUniqueId(), id -> this.builder.build(player));
    }

    /**
     * Removes a {@link HACPlayer} from the map by {@link java.util.UUID}.
     *
     * @param uuid The UUID of the Bukkit {@link org.bukkit.entity.Player}.
     * @return The removed {@link HACPlayer} instance.
     */
    public HACPlayer removePlayer(@NotNull final UUID uuid) {
        return this.players.remove(uuid);
    }

    /**
     * Removes a {@link HACPlayer} from the map by {@link org.bukkit.entity.Player}.
     *
     * @param player The {@link org.bukkit.entity.Player} object.
     * @return The removed {@link HACPlayer} instance.
     */
    public HACPlayer removePlayer(@NotNull final Player player) {
        return this.removePlayer(player.getUniqueId());
    }

    /**
     * Returns an {@link com.google.common.collect.ImmutableSet} copy of the current registered players.
     * This should be used strictly for looping through the current players.
     *
     * @return An {@link com.google.common.collect.ImmutableSet}
     */
    public Set<HACPlayer> getAll() {
        return ImmutableSet.copyOf(this.players.values());
    }

    /**
     * The {@link HACPlayerFactory} instance used to create {@link HACPlayer} instances.
     *
     * @return An {@link HACPlayerFactory} instance.
     */
    public HACPlayerFactory getBuilder() {
        return builder;
    }
}
