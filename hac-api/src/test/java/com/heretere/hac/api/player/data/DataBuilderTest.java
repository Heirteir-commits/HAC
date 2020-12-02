package com.heretere.hac.api.player.data;

import com.heretere.hac.api.events.AbilitiesPacketEventExecutorTest;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.api.player.builder.AbstractDataBuilder;

public final class DataBuilderTest extends AbstractDataBuilder<DataTest> {
    public DataBuilderTest() {
        super(new AbilitiesPacketEventExecutorTest());
    }

    @Override
    public DataTest build(HACPlayer player) {
        return new DataTest();
    }
}
