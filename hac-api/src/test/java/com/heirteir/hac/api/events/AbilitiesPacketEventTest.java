package com.heirteir.hac.api.events;

import com.heirteir.hac.api.events.packets.wrapper.in.AbilitiesPacket;
import com.heirteir.hac.api.player.HACPlayer;
import com.heirteir.hac.api.player.data.DataTest;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;

@Getter
public class AbilitiesPacketEventTest extends AbstractPacketEvent<AbilitiesPacket> {
    private boolean run1;
    private boolean run2;

    public AbilitiesPacketEventTest() {
        super(AbilitiesPacket.class, Priority.PROCESS_1);
    }

    @Override
    protected boolean update(HACPlayer player, @NotNull AbilitiesPacket packet) {
        player.getDataManager().getData(DataTest.class).setRan1(true);
        this.run1 = true;

        Assertions.assertFalse(packet.isFlying());

        return true;
    }

    @Override
    protected void onStop(HACPlayer player, @NotNull AbilitiesPacket packet) {
        this.run2 = true;
    }
}
