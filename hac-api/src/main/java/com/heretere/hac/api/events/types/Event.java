package com.heretere.hac.api.events.types;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public abstract class Event {
    private final Player player;
}
