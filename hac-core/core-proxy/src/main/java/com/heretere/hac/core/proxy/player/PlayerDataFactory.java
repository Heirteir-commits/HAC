/*
 * MIT License
 *
 * Copyright (c) 2021 Justin Heflin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.heretere.hac.core.proxy.player;

import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.api.player.factory.DataFactory;
import com.heretere.hac.core.proxy.player.executors.PlayerDataAbilitiesExecutor;
import com.heretere.hac.core.proxy.player.executors.PlayerDataEntityActionExecutor;
import com.heretere.hac.core.proxy.player.executors.PlayerDataFlyingExecutor;
import org.jetbrains.annotations.NotNull;

/**
 * The type Player data factory.
 */
public final class PlayerDataFactory extends DataFactory<PlayerData> {
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
            new PlayerDataAbilitiesExecutor(api, PlayerDataFactory.IDENTIFIER),
            new PlayerDataEntityActionExecutor(api, PlayerDataFactory.IDENTIFIER),
            new PlayerDataFlyingExecutor(api, PlayerDataFactory.IDENTIFIER)
        );
    }

    @Override
    public @NotNull PlayerData build(final @NotNull HACPlayer player) {
        return new PlayerData(player);
    }
}
