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

package com.heretere.hac.api.player;

import com.google.common.collect.Maps;
import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.player.factory.DataFactory;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * This class is responsible for adding data to the HACPlayer on creation. This data can then be accessed by a class
 * reference inside of {@link com.heretere.hac.api.player.factory.DataManager}.
 */
public final class HACPlayerFactory {
    /**
     * The HACAPI reference.
     */
    private final @NotNull HACAPI api;
    /**
     * The plugin housing the API.
     */
    private final @NotNull Plugin parent;
    /**
     * The HACPlayerList reference.
     */
    private final @NotNull HACPlayerList playerList;

    /**
     * A Map of the currently registered builders.
     */
    private final @NotNull Map<Class<?>, DataFactory<?>> builders;

    HACPlayerFactory(
        final @NotNull HACAPI api,
        final @NotNull Plugin parent,
        final @NotNull HACPlayerList playerList
    ) {
        this.api = api;
        this.parent = parent;
        this.playerList = playerList;
        this.builders = Maps.newLinkedHashMap();
    }

    /**
     * Registers a {@link DataFactory} to be added to the
     * {@link com.heretere.hac.api.player.factory.DataManager} instance. This will also add the data to all active
     * {@link com.heretere.hac.api.player.factory.DataManager} instances as well.
     *
     * @param <T>     The {@link java.lang.Class} the {@link DataFactory} outputs.
     * @param clazz   The {@link java.lang.Class} the {@link DataFactory} outputs.
     * @param builder The {@link DataFactory} instance.
     */
    public <T> void registerDataBuilder(
        final @NotNull Class<T> clazz,
        final @NotNull DataFactory<T> builder
    ) {
        this.builders.put(clazz, builder);
        this.playerList.getAll().forEach(player -> player.getDataManager().addDataRaw(clazz, builder.build(player)));
        builder.registerUpdaters();
    }


    /**
     * Unregisters a {@link DataFactory} from all {@link com.heretere.hac.api.player.factory.DataManager}
     * instances.
     *
     * @param clazz The {@link java.lang.Class} used to register the {@link DataFactory} initially.
     */
    public void unregisterDataBuilder(final @NotNull Class<?> clazz) {
        DataFactory<?> builder = this.builders.remove(clazz);

        if (builder != null) {
            builder.unregisterUpdaters();

            this.playerList.getAll().forEach(player -> player.getDataManager().removeData(clazz));
        }
    }

    /**
     * Used in {@link HACPlayerList#getPlayer(org.bukkit.entity.Player)} to populate the
     * {@link com.heretere.hac.api.player.factory.DataManager} with built data instances.
     * Then, it outputs a completely built {@link HACPlayer}.
     *
     * @param player The Bukkit {@link org.bukkit.entity.Player}.
     * @return The built {@link HACPlayer}.
     */
    public @NotNull HACPlayer build(final @NotNull Player player) {
        HACPlayer hacPlayer = new HACPlayer(this.api, this.parent, player);
        this.builders.forEach((key, value) -> hacPlayer.getDataManager().addDataRaw(key, value.build(hacPlayer)));
        return hacPlayer;
    }
}
