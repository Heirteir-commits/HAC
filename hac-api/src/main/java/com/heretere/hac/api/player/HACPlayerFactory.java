package com.heretere.hac.api.player;

import com.google.common.collect.Maps;
import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.player.builder.AbstractDataFactory;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * This class is responsible for adding data to the HACPlayer on creation. This data can then be accessed by a class
 * reference inside of {@link com.heretere.hac.api.player.builder.DataManager}.
 */
public final class HACPlayerFactory {
    /**
     * The HACAPI reference.
     */
    private final HACAPI api;
    /**
     * The HACPlayerList reference.
     */
    private final HACPlayerList playerList;

    /**
     * A Map of the currently registered builders.
     */
    private final Map<Class<?>, AbstractDataFactory<?>> builders;

    HACPlayerFactory(
            @NotNull final HACAPI api,
            @NotNull final HACPlayerList playerList
    ) {
        this.api = api;
        this.playerList = playerList;
        this.builders = Maps.newLinkedHashMap();
    }


    /**
     * Registers a {@link AbstractDataFactory} to be added to the
     * {@link com.heretere.hac.api.player.builder.DataManager} instance. This will also add the data to all active
     * {@link com.heretere.hac.api.player.builder.DataManager} instances as well.
     *
     * @param <T>     The {@link java.lang.Class} the {@link AbstractDataFactory} outputs.
     * @param clazz   The {@link java.lang.Class} the {@link AbstractDataFactory} outputs.
     * @param builder The {@link AbstractDataFactory} instance.
     */
    public <T> void registerDataBuilder(
            @NotNull final Class<T> clazz,
            final @NotNull AbstractDataFactory<T> builder
    ) {
        this.builders.put(clazz, builder);
        this.playerList.getAll().forEach(player -> player.getDataManager().addDataRaw(clazz, builder.build(player)));
        builder.registerUpdaters();
    }


    /**
     * Unregisters a {@link AbstractDataFactory}0rom all {@link com.heretere.hac.api.player.builder.DataManager}
     * instances.
     *
     * @param clazz The {@link java.lang.Class} used to register the {@link AbstractDataFactory}
     *              initially.
     */
    public void unregisterDataBuilder(@NotNull final Class<?> clazz) {
        AbstractDataFactory<?> builder = this.builders.remove(clazz);

        if (builder != null) {
            builder.unregisterUpdaters();

            this.playerList.getAll().forEach(player -> player.getDataManager().removeData(clazz));
        }
    }

    /**
     * Used in {@link HACPlayerList#getPlayer(org.bukkit.entity.Player)} to populate the
     * {@link com.heretere.hac.api.player.builder.DataManager} with built data instances.
     * Then, it outputs a completely built {@link HACPlayer}.
     *
     * @param player The Bukkit {@link org.bukkit.entity.Player}.
     * @return The built {@link HACPlayer}.
     */
    public HACPlayer build(@NotNull final Player player) {
        HACPlayer hacPlayer = new HACPlayer(this.api, player);
        this.builders.forEach((key, value) -> hacPlayer.getDataManager().addDataRaw(key, value.build(hacPlayer)));
        return hacPlayer;
    }
}
