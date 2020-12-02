package com.heretere.hac.api.events.types.packets.wrapper.clientside;

import com.heretere.hac.api.events.types.packets.PacketConstants;
import com.heretere.hac.api.events.types.packets.wrapper.AbstractWrappedPacketIn;

import java.util.Arrays;

/**
 * This is the wrapped version of the PacketPlayInEntityActionPacket.
 */
public abstract class EntityActionPacket extends AbstractWrappedPacketIn {
    private Action action;

    /**
     * Instantiates a new Entity action packet.
     */
    public EntityActionPacket() {
        super(PacketConstants.In.ENTITY_ACTION);
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

        /**
         * Used to map an NMS Entity Action enum to {@link Action}
         *
         * @param name the name
         * @return the action
         */
        public static Action fromString(String name) {
            return Arrays.stream(Action.values())
                    .filter(action -> action.name().equals(name))
                    .findFirst()
                    .orElse(INVALID);
        }
    }
}
