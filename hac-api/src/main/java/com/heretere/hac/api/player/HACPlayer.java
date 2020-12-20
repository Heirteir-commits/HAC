package com.heretere.hac.api.player;

import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.concurrency.ThreadPool;
import com.heretere.hac.api.player.builder.DataManager;
import org.bukkit.Bukkit;
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
    private final HACAPI api;
    private CompletableFuture<Void> future;

    private final WeakReference<Player> player;
    private final DataManager dataManager;

    HACPlayer(@NotNull HACAPI api, @NotNull Player player) {
        this.api = api;

        this.future = CompletableFuture.allOf();

        this.player = new WeakReference<>(player);

        this.dataManager = new DataManager();
    }

    /**
     * This passes a runnable to the {@link ThreadPool}. Anything passed to this method is ran in
     * guaranteed serial order.
     *
     * @param runnable     The runnable to run in the {@link ThreadPool}.
     * @param errorHandler if null it uses the hac-api error handler. Otherwise it uses the supplied error handler.
     */
    public void runTaskASync(@NotNull Runnable runnable, @Nullable BiConsumer<? super Void, ? super Throwable> errorHandler) {
        this.future = this.future
                .thenRunAsync(runnable, this.api.getThreadPool().getPool())
                .whenCompleteAsync(
                        errorHandler == null ?
                                (msg, ex) -> {
                                    if (ex != null) {
                                        this.api.getErrorHandler().getHandler().accept(ex);
                                    }
                                } :
                                errorHandler,
                        this.api.getThreadPool().getPool());
    }

    /**
     * The {@link java.util.UUID} of the player used for the HACPlayer
     *
     * @return The UUID of the player.
     */
    public UUID getUUID() {
        return this.getBukkitPlayer().getUniqueId();
    }

    /**
     * Get's the player instance using {@link Bukkit#getPlayer(UUID)}. Should only be used at init or destroy of HACPlayer.
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
