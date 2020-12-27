package com.heretere.hac.api.player;

import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.player.factory.DataManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Optional;
import java.util.UUID;

/**
 * {@link HACPlayer} is a wrapper used to handle all data related to {@link org.bukkit.entity.Player}
 * for the plugin.
 */
public final class HACPlayer {
    /**
     * The UUID of the player.
     */
    private final @NotNull UUID uuid;
    /**
     * A WeakReference to the player to make sure this class doesn't stop the player from being garbage collected.
     */
    private final @NotNull Reference<Player> player;
    /**
     * The data manager instance for this HACPlayer.
     */
    private final @NotNull DataManager dataManager;
    /**
     * The chained CompletableFutures. The goal of this is to allow for a thread per player design approach.
     * Without created a bunch of new threads on the server.
     */
    private final @NotNull FutureChain futureChain;

    HACPlayer(
        final @NotNull HACAPI api,
        final @NotNull Plugin parent,
        final @NotNull Player player
    ) {
        this.futureChain = new FutureChain(api, parent);
        this.uuid = player.getUniqueId();
        this.player = new WeakReference<>(player);
        this.dataManager = new DataManager();
    }

    /**
     * The {@link java.util.UUID} of the player used for the HACPlayer.
     *
     * @return The UUID of the player.
     */
    public @NotNull UUID getUUID() {
        return this.uuid;
    }

    /**
     * Get's the player instance using {@link org.bukkit.Bukkit#getPlayer(UUID)}.
     * Should only be used at init or destroy of HACPlayer.
     *
     * @return The Bukkit Player
     */
    public @NotNull Optional<Player> getBukkitPlayer() {
        return Optional.ofNullable(this.player.get());
    }

    /**
     * The {@link DataManager} instance for this player.
     *
     * @return The Data Manager.
     */
    public @NotNull DataManager getDataManager() {
        return this.dataManager;
    }

    /**
     * The Future Chain allows for tasks to be executed in a pseudo single thread.
     *
     * @return The future chain
     */
    public @NotNull FutureChain getFutureChain() {
        return this.futureChain;
    }
}
