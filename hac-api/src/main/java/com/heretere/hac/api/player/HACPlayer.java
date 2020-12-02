package com.heretere.hac.api.player;

import com.heretere.hac.api.API;
import com.heretere.hac.api.player.builder.DataManager;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

@Getter
public final class HACPlayer {
    private CompletableFuture<Void> future;

    private final DataManager dataManager;
    private final Player bukkitPlayer;


    public HACPlayer(Player player) {
        this.future = CompletableFuture.allOf();

        this.dataManager = new DataManager();
        this.bukkitPlayer = player;
    }

    public void runTask(Runnable runnable, @Nullable BiConsumer<? super Void, ? super Throwable> errorHandler) {
        this.future = this.future
                .thenRunAsync(runnable, API.INSTANCE.getThreadPool().getPool())
                .whenCompleteAsync(errorHandler == null ? API.INSTANCE.getThreadPool().getDefaultErrorHandler() : errorHandler, API.INSTANCE.getThreadPool().getPool());
    }
}
