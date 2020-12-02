package com.heirteir.hac.api;

import com.heirteir.hac.api.events.ASyncPacketEventManager;
import com.heirteir.hac.api.player.HACPlayerList;
import com.heirteir.hac.api.util.reflections.Reflections;
import lombok.Getter;

@Getter
@SuppressWarnings("ImmutableEnumChecker")
public enum API {
    INSTANCE;

    private final Reflections reflections;
    private final ASyncPacketEventManager eventManager;
    private final HACPlayerList hacPlayerList;

    API() {
        this.reflections = new Reflections();
        this.eventManager = new ASyncPacketEventManager();
        this.hacPlayerList = new HACPlayerList();
    }
}
