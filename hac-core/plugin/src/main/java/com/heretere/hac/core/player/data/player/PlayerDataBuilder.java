package com.heretere.hac.core.player.data.player;

import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.api.player.builder.AbstractDataBuilder;
import com.heretere.hac.core.player.data.player.executors.PlayerDataAbilitiesExecutor;
import com.heretere.hac.core.player.data.player.executors.PlayerDataEntityActionExecutor;
import com.heretere.hac.core.player.data.player.executors.PlayerDataFlyingExecutor;

public final class PlayerDataBuilder extends AbstractDataBuilder<PlayerData> {
    public PlayerDataBuilder() {
        super(
                new PlayerDataAbilitiesExecutor(),
                new PlayerDataEntityActionExecutor(),
                new PlayerDataFlyingExecutor()
        );
    }

    @Override
    public PlayerData build(HACPlayer player) {
        return new PlayerData(player);
    }
}
