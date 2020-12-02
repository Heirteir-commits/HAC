package com.heirteir.hac.core.player.data.location;

import com.heirteir.hac.api.player.HACPlayer;
import com.heirteir.hac.api.player.builder.AbstractDataBuilder;
import com.heirteir.hac.core.player.data.location.updaters.AbilitiesUpdater;
import com.heirteir.hac.core.player.data.location.updaters.EntityActionUpdater;
import com.heirteir.hac.core.player.data.location.updaters.FlyingUpdater;

public class PlayerDataBuilder extends AbstractDataBuilder<PlayerData> {

    public PlayerDataBuilder() {
        super(new AbilitiesUpdater(),
                new EntityActionUpdater(),
                new FlyingUpdater());
    }

    @Override
    public PlayerData build(HACPlayer player) {
        return new PlayerData(player);
    }
}
