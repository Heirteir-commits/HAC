package com.heirteir.hac.api.events;

import com.heirteir.hac.api.events.packets.wrapper.out.EntityVelocityPacket;
import com.heirteir.hac.api.player.HACPlayer;
import com.heirteir.hac.api.player.data.DataTest;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;

@Getter
public final class EntityVelocityEventTest extends AbstractPacketEvent<EntityVelocityPacket> {
    private boolean run1;
    private boolean run2;

    public EntityVelocityEventTest() {
        super(EntityVelocityPacket.class, Priority.PROCESS_3);
    }

    @Override
    protected boolean update(HACPlayer player, @NotNull EntityVelocityPacket packet) {
        player.getDataManager().getData(DataTest.class).setRan3(true);
        this.run1 = true;

        Assertions.assertEquals(0, packet.getX());
        Assertions.assertEquals(0, packet.getY());
        Assertions.assertEquals(0, packet.getZ());

        return true;
    }

    @Override
    protected void onStop(HACPlayer player, @NotNull EntityVelocityPacket packet) {
        this.run2 = true;
    }
}
