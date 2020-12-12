package com.heretere.hac.api.concurrency;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.heretere.hac.api.HACAPI;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This is the ThreadPool instance used for multi-threading the tasks ran inside of HAC.
 * You can get an the default instance from {@link HACAPI#getThreadPool()}.
 */
public class ThreadPool {
    private final ExecutorService pool;

    /**
     * Instantiates a new Thread pool.
     */
    public ThreadPool() {
        this.pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new ThreadFactoryBuilder().setNameFormat("hac-thread-%d").build());
    }

    /**
     * Get the {@link ExecutorService}
     *
     * @return the {@link ExecutorService}
     */
    public ExecutorService getPool() {
        return pool;
    }

    /**
     * Calls {@link ExecutorService#shutdownNow()} to insure that when the plugin is disabled all tasks are cancelled.
     * As they are no longer needed.
     */
    public void unload() {
        this.pool.shutdownNow();
    }
}
