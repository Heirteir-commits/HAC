package com.heretere.hac.core.proxy.player.data.player;

import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.api.player.builder.AbstractDataBuilder;
import com.heretere.hac.core.proxy.player.data.player.executors.PlayerDataAbilitiesExecutor;
import com.heretere.hac.core.proxy.player.data.player.executors.PlayerDataEntityActionExecutor;
import com.heretere.hac.core.proxy.player.data.player.executors.PlayerDataFlyingExecutor;
import org.jetbrains.annotations.NotNull;

public final class PlayerDataBuilder extends AbstractDataBuilder<PlayerData> {
    private static final String IDENTIFIER = "player_data";

    public PlayerDataBuilder(@NotNull HACAPI api) {
        super(
                api,
                new PlayerDataAbilitiesExecutor(PlayerDataBuilder.IDENTIFIER),
                new PlayerDataEntityActionExecutor(PlayerDataBuilder.IDENTIFIER),
                new PlayerDataFlyingExecutor(PlayerDataBuilder.IDENTIFIER)
        );
    }

    @Override
    public PlayerData build(@NotNull HACPlayer player) {
        return new PlayerData(player);
    }
}
