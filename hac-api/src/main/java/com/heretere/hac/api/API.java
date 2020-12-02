package com.heretere.hac.api;

import com.heretere.hac.api.concurrency.ThreadPool;
import com.heretere.hac.api.events.ASyncPacketEventManager;
import com.heretere.hac.api.player.HACPlayerList;
import com.heretere.hac.api.util.reflections.Reflections;
import lombok.Getter;

@Getter
@SuppressWarnings("ImmutableEnumChecker")
public enum API {
    INSTANCE;

    private final Reflections reflections;
    private final ASyncPacketEventManager eventManager;
    private final HACPlayerList hacPlayerList;
    private final ThreadPool threadPool;

    API() {
        this.reflections = new Reflections();
        this.eventManager = new ASyncPacketEventManager();
        this.hacPlayerList = new HACPlayerList();
        this.threadPool = new ThreadPool();
    }
}
