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
        this.chain = this.chain.thenRunAsync(runnable, this.api.getThreadPool()).whenCompleteAsync(
            (msg, ex) -> this.api.getErrorHandler().getHandler().accept(ex),
            this.api.getThreadPool()
        );
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
                try {
                    runnable.run();
                } catch (Exception e) {
                    this.api.getErrorHandler().getHandler().accept(e);
                } finally {
                    latch.countDown();
                }
            });

            try {
                latch.await();
            } catch (InterruptedException e) {
                this.api.getErrorHandler().getHandler().accept(e);
                Thread.currentThread().interrupt();
            }
        }, this.api.getThreadPool()).whenCompleteAsync(
            (msg, ex) -> this.api.getErrorHandler().getHandler().accept(ex),
            this.api.getThreadPool()
        );
    }
}
