package com.heretere.hac.core.proxy.player;

import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.api.player.factory.AbstractDataFactory;
import com.heretere.hac.core.proxy.player.executors.PlayerDataAbilitiesExecutor;
import com.heretere.hac.core.proxy.player.executors.PlayerDataEntityActionExecutor;
import com.heretere.hac.core.proxy.player.executors.PlayerDataFlyingExecutor;
import org.jetbrains.annotations.NotNull;

/**
 * The type Player data factory.
 */
public final class PlayerDataFactory extends AbstractDataFactory<PlayerData> {
    /**
     * The base identifier attached to all executors of this class.
     */
    private static final @NotNull String IDENTIFIER = "player_data";

    /**
     * Instantiates a new Player data factory.
     *
     * @param api the api
     */
    public PlayerDataFactory(final @NotNull HACAPI api) {
        super(
            api,
            new PlayerDataAbilitiesExecutor(PlayerDataFactory.IDENTIFIER),
            new PlayerDataEntityActionExecutor(PlayerDataFactory.IDENTIFIER),
            new PlayerDataFlyingExecutor(PlayerDataFactory.IDENTIFIER)
        );
    }

    @Override
    public @NotNull PlayerData build(final @NotNull HACPlayer player) {
        return new PlayerData(player);
    }
}
