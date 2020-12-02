package com.heretere.hac.api.events;

import com.heretere.hac.api.events.types.Priority;
import com.heretere.hac.api.events.types.packets.AbstractPacketEventExecutor;
import com.heretere.hac.api.events.types.packets.wrapper.in.AbilitiesPacket;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.api.player.data.DataTest;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;

@Getter
public final class AbilitiesPacketEventExecutorTest extends AbstractPacketEventExecutor<AbilitiesPacket> {
    private boolean run1;
    private boolean run2;

    public AbilitiesPacketEventExecutorTest() {
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
