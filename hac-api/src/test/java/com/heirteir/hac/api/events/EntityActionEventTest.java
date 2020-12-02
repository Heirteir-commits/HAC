package com.heirteir.hac.api.events;

import com.heirteir.hac.api.events.packets.wrapper.in.EntityActionPacket;
import com.heirteir.hac.api.player.HACPlayer;
import com.heirteir.hac.api.player.data.DataTest;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;

@Getter
public final class EntityActionEventTest extends AbstractPacketEvent<EntityActionPacket> {
    private boolean run1;
    private boolean run2;

    public EntityActionEventTest() {
        super(EntityActionPacket.class, Priority.PROCESS_2);
    }

    @Override
    protected boolean update(HACPlayer player, @NotNull EntityActionPacket packet) {
        player.getDataManager().getData(DataTest.class).setRan2(true);
        this.run1 = true;

        Assertions.assertEquals(EntityActionPacket.Action.INVALID, packet.getAction());

        return true;
    }

    @Override
    protected void onStop(HACPlayer player, @NotNull EntityActionPacket packet) {
        this.run2 = true;
    }
}
