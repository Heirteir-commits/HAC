package com.heretere.hac.api.events.types;

import org.bukkit.entity.Player;

/**
 * The type Event.
 */
public abstract class Event {
    private final Player player;

    /**
     * Instantiates a new Event.
     *
     * @param player the player
     */
    public Event(Player player) {
        this.player = player;
    }

    /**
     * Gets player.
     *
     * @return the player
     */
    public Player getPlayer() {
        return this.player;
    }
}
