package com.heretere.hac.core.player.data.location;

import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.api.player.builder.AbstractDataBuilder;
import com.heretere.hac.core.player.data.location.updaters.PlayerDataAbilitiesUpdater;
import com.heretere.hac.core.player.data.location.updaters.PlayerDataEntityActionUpdater;
import com.heretere.hac.core.player.data.location.updaters.PlayerDataFlyingUpdater;

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
