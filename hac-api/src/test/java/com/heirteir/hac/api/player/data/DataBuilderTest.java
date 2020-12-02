package com.heirteir.hac.api.player.data;

import com.heirteir.hac.api.events.AbilitiesPacketEventTest;
import com.heirteir.hac.api.player.HACPlayer;
import com.heirteir.hac.api.player.builder.AbstractDataBuilder;

public class DataBuilderTest extends AbstractDataBuilder<DataTest> {
    public DataBuilderTest() {
        super(new AbilitiesPacketEventTest());
    }

    @Override
    public DataTest build(HACPlayer player) {
        return new DataTest();
    }
}
