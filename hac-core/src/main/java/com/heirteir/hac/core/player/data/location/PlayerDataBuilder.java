package com.heirteir.hac.core.player.data.location;

import com.heirteir.hac.api.player.HACPlayer;
import com.heirteir.hac.api.player.builder.AbstractDataBuilder;
import com.heirteir.hac.core.player.data.location.updaters.PlayerDataAbilitiesUpdater;
import com.heirteir.hac.core.player.data.location.updaters.PlayerDataEntityActionUpdater;
import com.heirteir.hac.core.player.data.location.updaters.PlayerDataFlyingUpdater;

public final class PlayerDataBuilder extends AbstractDataBuilder<PlayerData> {

    public PlayerDataBuilder() {
        super(new PlayerDataAbilitiesUpdater(),
                new PlayerDataEntityActionUpdater(),
                new PlayerDataFlyingUpdater());
    }

    @Override
    public PlayerData build(HACPlayer player) {
        return new PlayerData(player);
    }
}
