package com.heretere.hac.api.player;

import com.heretere.hac.api.HACAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

/**
 * This class is responsible for ensuring tasks sent towards the player are ran in a serialized order.
 */
public final class FutureChain {
    /**
     * The API instance.
     */
    private final @NotNull HACAPI api;
    /**
     * The plugin that is housing the API.
     */
    private final @NotNull Plugin parent;

    /**
     * The future chain this is used to mimic a single thread even if tasks happen on different threads.
     */
    private @NotNull CompletableFuture<Void> chain;

    /**
     * Creates a new Future Chain instance.
     *
     * @param api    the API instance
     * @param parent The plugin housing the API
     */
    FutureChain(
        final @NotNull HACAPI api,
        final @NotNull Plugin parent
    ) {
        this.api = api;
        this.parent = parent;
        this.chain = CompletableFuture.allOf();
    }

    /**
     * Runs a task in a cached thread pool off the main server thread.
     *
     * @param runnable the runnable to execute
     */
    public void addAsyncTask(final @NotNull Runnable runnable) {
        this.chain = this.chain.thenRunAsync(runnable, this.api.getThreadPool());
        this.attachErrorHandler();
    }

    /**
     * Runs a task on the main server thread.
     *
     * @param runnable the runnable to execute
     */
    public void addServerMainThreadTask(final @NotNull Runnable runnable) {
        this.chain = this.chain.thenRunAsync(() -> {
            CountDownLatch latch = new CountDownLatch(1);

            Bukkit.getScheduler().scheduleSyncDelayedTask(this.parent, () -> {
                runnable.run();
                latch.countDown();
            });

            try {
                latch.await();
            } catch (InterruptedException e) {
                this.api.getErrorHandler().getHandler().accept(e);
                Thread.currentThread().interrupt();
            }
        }, this.api.getThreadPool());
        this.attachErrorHandler();
    }

    private synchronized void attachErrorHandler() {
        this.chain = this.chain.whenCompleteAsync((msg, ex) -> {
            if (ex != null) {
                this.api
                    .getErrorHandler()
                    .getHandler()
                    .accept(ex);
            }
        }, this.api.getThreadPool());
    }
}
