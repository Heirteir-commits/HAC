package com.heretere.hac.api.events.packets.wrapper.clientside;

import com.heretere.hac.api.events.packets.wrapper.WrappedPacketIn;
import org.jetbrains.annotations.NotNull;

/**
 * This is the wrapped version of the PacketPlayInEntityActionPacket.
 */
public final class EntityActionPacket implements WrappedPacketIn {
    /**
     * The action of this packet.
     */
    private final Action action;

    /**
     * Instantiates a new Entity action packet.
     *
     * @param action The action
     */
    public EntityActionPacket(@NotNull final Action action) {
        this.action = action;
    }

    /**
     * Gets action.
     *
     * @return the action
     */
    public Action getAction() {
        return this.action;
    }

    /**
     * This maps the Action enum from PacketPlayInEntityAction.
     */
    public enum Action {
        /**
         * Start sneaking action.
         */
        START_SNEAKING,
        /**
         * Stop sneaking action.
         */
        STOP_SNEAKING,
        /**
         * Start sprinting action.
         */
        START_SPRINTING,
        /**
         * Stop sprinting action.
         */
        STOP_SPRINTING,
        /**
         * Start flying with Elytra.
         */
        START_FALL_FLYING,
        /**
         * This is passed if the passed in action wasn't any of the above.
         */
        INVALID
    }
}
