package com.heirteir.hac.api.events;

import com.heirteir.hac.api.events.packets.wrapper.in.FlyingPacket;
import com.heirteir.hac.api.player.HACPlayer;
import com.heirteir.hac.api.player.data.DataTest;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;

@Getter
public class FlyingPacketEventTest extends AbstractPacketEvent<FlyingPacket> {
    private boolean run1;
    private boolean run2;

    public FlyingPacketEventTest() {
        super(FlyingPacket.class, Priority.PROCESS_4);
    }

    @Override
    protected boolean update(HACPlayer player, @NotNull FlyingPacket packet) {
        player.getDataManager().getData(DataTest.class).setRan4(true);
        run1 = true;

        Assertions.assertEquals(0, packet.getX());
        Assertions.assertEquals(0, packet.getY());
        Assertions.assertEquals(0, packet.getZ());
        Assertions.assertEquals(0, packet.getYaw());
        Assertions.assertEquals(0, packet.getPitch());
        Assertions.assertFalse(packet.isHasLook());
        Assertions.assertTrue(packet.isHasPos());
        Assertions.assertTrue(packet.isOnGround());

        return false;
    }

    @Override
    protected void onStop(HACPlayer player, @NotNull FlyingPacket packet) {
        run2 = true;
    }
}
