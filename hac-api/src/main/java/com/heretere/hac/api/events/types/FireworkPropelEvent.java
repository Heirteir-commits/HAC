package com.heretere.hac.api.events.types;

import org.bukkit.entity.Player;

/**
 * The type Firework propel event.
 */
public class FireworkPropelEvent extends Event {
    /**
     * Instantiates a new Firework propel event.
     *
     * @param entity the entity
     */
    public FireworkPropelEvent(Player entity) {
        super(entity);
    }
}
