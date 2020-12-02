package com.heirteir.hac.api.player.data;

import com.heirteir.hac.api.events.AbilitiesPacketEventExecutorTest;
import com.heirteir.hac.api.player.HACPlayer;
import com.heirteir.hac.api.player.builder.AbstractDataBuilder;

public final class DataBuilderTest extends AbstractDataBuilder<DataTest> {
    public DataBuilderTest() {
        super(new AbilitiesPacketEventExecutorTest());
    }

    @Override
    public DataTest build(HACPlayer player) {
        return new DataTest();
    }
}
