package com.heretere.hac.api.player;

import com.google.common.base.Preconditions;
import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.concurrency.ThreadPool;
import com.heretere.hac.api.player.builder.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

/**
 * {@link HACPlayer} is a wrapper used to handle all data related to {@link org.bukkit.entity.Player}
 * for the plugin.
 */
public final class HACPlayer {
    private CompletableFuture<Void> future;

    private final UUID uuid;
    private final DataManager dataManager;

    HACPlayer(@NotNull Player player) {
        Preconditions.checkNotNull(player, "Player is null.");

        this.future = CompletableFuture.allOf();

        this.uuid = player.getUniqueId();

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
                .thenRunAsync(runnable, HACAPI.getInstance().getThreadPool().getPool())
                .whenCompleteAsync(
                        errorHandler == null ?
                                (msg, ex) -> {
                                    if (ex != null) {
                                        HACAPI.getInstance().getErrorHandler().getHandler().accept(ex);
                                    }
                                } :
                                errorHandler,
                        HACAPI.getInstance().getThreadPool().getPool());
    }

    /**
     * The {@link java.util.UUID} of the player used for the HACPlayer
     *
     * @return The UUID of the player.
     */
    public UUID getUUID() {
        return uuid;
    }

    /**
     * Get's the player instance using {@link Bukkit#getPlayer(UUID)}. Should only be used at init or destroy of HACPlayer.
     *
     * @return The Bukkit Player
     */
    public Player getBukkitPlayer() {
        Player player = Bukkit.getPlayer(uuid);

        Preconditions.checkNotNull(player, "Player with this UUID is offline.");

        return player;
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
