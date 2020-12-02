package com.heretere.hac.api.events.types;


import com.heretere.hac.api.player.HACPlayer;

/**
 * The type Abstract event executor.
 *
 * @param <T> the type parameter
 */
public abstract class AbstractEventExecutor<T extends Event> {
    private final Class<T> eventClass;
    private final Priority priority;

    /**
     * Instantiates a new Abstract event executor.
     *
     * @param eventClass the event class
     * @param priority   the priority
     */
    protected AbstractEventExecutor(Class<T> eventClass, Priority priority) {
        this.eventClass = eventClass;
        this.priority = priority;
    }

    /**
     * Run.
     *
     * @param player the player
     * @param event  the event
     */
    public final void run(HACPlayer player, Object event) {
        this.run(player, this.eventClass.cast(event));
    }

    /**
     * Run.
     *
     * @param player the player
     * @param event  the event
     */
    protected abstract void run(HACPlayer player, T event);

    /**
     * Gets event class.
     *
     * @return the event class
     */
    public Class<T> getEventClass() {
        return this.eventClass;
    }

    /**
     * Gets priority.
     *
     * @return the priority
     */
    public Priority getPriority() {
        return this.priority;
    }
}
