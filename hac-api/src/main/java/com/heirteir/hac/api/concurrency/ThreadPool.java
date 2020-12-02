package com.heirteir.hac.api.concurrency;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

public class ThreadPool {
    @Getter
    private final ExecutorService pool;
    @Getter
    @Setter
    private BiConsumer<? super Void, ? super Throwable> defaultErrorHandler;

    public ThreadPool() {
        this.pool = Executors.newCachedThreadPool(new ThreadFactoryBuilder().setNameFormat("hac-cached-thread-%d").build());
    }

    public void unload() {
        this.pool.shutdownNow();
    }

}
