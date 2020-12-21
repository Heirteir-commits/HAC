package com.heretere.hac.api.player;

import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.player.builder.DataManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

/**
 * {@link HACPlayer} is a wrapper used to handle all data related to {@link org.bukkit.entity.Player}
 * for the plugin.
 */
public final class HACPlayer {
    /**
     * The HAC API reference.
     */
    private final HACAPI api;
    /**
     * A WeakReference to the player to make sure this class doesn't stop the player from being garbage collected.
     */
    private final WeakReference<Player> player;
    /**
     * The data manager instance for this HACPlayer.
     */
    private final DataManager dataManager;
    /**
     * The chained CompletableFutures. The goal of this is to allow for a thread per player design approach.
     * Without created a bunch of new threads on the server.
     */
    private CompletableFuture<Void> future;

    /**
     * Instantiates a new Hac player.
     *
     * @param api    the api
     * @param player the player
     */
    HACPlayer(@NotNull final HACAPI api, @NotNull final Player player) {
        this.api = api;

        this.future = CompletableFuture.allOf();

        this.player = new WeakReference<>(player);

        this.dataManager = new DataManager();
    }

    /**
     * This passes a runnable to the {@link com.heretere.hac.api.concurrency.ThreadPool}.
     * Anything passed to this method is ran in guaranteed serial order.
     *
     * @param runnable     The runnable to run in the {@link com.heretere.hac.api.concurrency.ThreadPool}.
     * @param errorHandler if null it uses the hac-api error handler. Otherwise it uses the supplied error handler.
     */
    public void runTaskASync(@NotNull final Runnable runnable,
                             @Nullable final BiConsumer<? super Void, ? super Throwable> errorHandler) {
        this.future = this.future
                .thenRunAsync(runnable, this.api.getThreadPool().getPool())
                .whenCompleteAsync(
                        errorHandler == null
                                ? (msg, ex) -> {
                            if (ex != null) {
                                this.api.getErrorHandler().getHandler().accept(ex);
                            }
                        }
                                : errorHandler,
                        this.api.getThreadPool().getPool());
    }

    /**
     * The {@link java.util.UUID} of the player used for the HACPlayer.
     *
     * @return The UUID of the player.
     */
    public UUID getUUID() {
        return this.getBukkitPlayer().getUniqueId();
    }

    /**
     * Get's the player instance using {@link org.bukkit.Bukkit#getPlayer(UUID)}.
     * Should only be used at init or destroy of HACPlayer.
     *
     * @return The Bukkit Player
     */
    public Player getBukkitPlayer() {
        return this.player.get();
    }

    /**
     * The {@link DataManager} instance for this player.
     *
     * @return The Data Manager.
     */
    public DataManager getDataManager() {
        return dataManager;
    }
}
