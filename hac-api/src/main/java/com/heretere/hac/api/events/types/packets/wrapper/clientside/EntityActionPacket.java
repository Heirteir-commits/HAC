package com.heretere.hac.api.events.types.packets.wrapper.clientside;

import com.heretere.hac.api.events.types.packets.wrapper.AbstractWrappedPacketIn;

/**
 * This is the wrapped version of the PacketPlayInEntityActionPacket.
 */
public final class EntityActionPacket extends AbstractWrappedPacketIn {
    private final Action action;

    /**
     * @param action The action
     */
    public EntityActionPacket(Action action) {
        this.action = action;
    }

    public Action getAction() {
        return action;
    }

    /**
     * This maps the Action enum from PacketPlayInEntityAction
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
         * Start flying with Elytra
         */
        START_FALL_FLYING,
        /**
         * This is passed if the passed in action wasn't any of the above.
         */
        INVALID;
    }
}
