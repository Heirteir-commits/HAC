package com.heretere.hac.api.player;

import com.heretere.hac.api.player.builder.AbstractDataBuilder;
import com.google.common.collect.Maps;
import com.heretere.hac.api.player.builder.DataManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * This class is responsible for adding data to the HACPlayer on creation. This data can then be accessed by a class
 * reference inside of {@link DataManager}.
 */
public final class HACPlayerBuilder {
    private final HACPlayerList playerList;

    private final Map<Class<?>, AbstractDataBuilder<?>> builders;

    HACPlayerBuilder(@NotNull HACPlayerList playerList) {
        this.playerList = playerList;
        this.builders = Maps.newLinkedHashMap();
    }


    /**
     * Registers a {@link AbstractDataBuilder} to be added to the
     * {@link DataManager} instance. This will also add the data to all active
     * {@link DataManager} instances as well.
     *
     * @param <T>     The {@link java.lang.Class} the {@link AbstractDataBuilder} outputs.
     * @param clazz   The {@link java.lang.Class} the {@link AbstractDataBuilder} outputs.
     * @param builder The {@link AbstractDataBuilder} instance.
     */
    public <T> void registerDataBuilder(@NotNull Class<T> clazz, @NotNull AbstractDataBuilder<T> builder) {
        this.builders.put(clazz, builder);
        this.playerList.getAll().forEach(player -> player.getDataManager().addDataRaw(clazz, builder.build(player)));
        builder.registerUpdaters();
    }


    /**
     * Unregisters a {@link AbstractDataBuilder} from all {@link DataManager}
     * instances.
     *
     * @param clazz The {@link java.lang.Class} used to register the {@link AbstractDataBuilder}
     *              initially.
     */
    public void unregisterDataBuilder(@NotNull Class<?> clazz) {
        AbstractDataBuilder<?> builder = this.builders.remove(clazz);

        if (builder != null) {
            builder.unregisterUpdaters();

            this.playerList.getAll()
                    .forEach(player -> player.getDataManager().removeData(clazz));
        }
    }

    /**
     * Used in {@link HACPlayerList#getPlayer(org.bukkit.entity.Player)} to populate the
     * {@link DataManager} with built data instances. Then, it outputs a completely built
     * {@link HACPlayer}.
     *
     * @param player The Bukkit {@link org.bukkit.entity.Player}.
     * @return The built {@link HACPlayer}.
     */
    public HACPlayer build(@NotNull Player player) {
        HACPlayer hacPlayer = new HACPlayer(player);
        this.builders
                .forEach((key, value) -> hacPlayer.getDataManager().addDataRaw(key, value.build(hacPlayer)));
        return hacPlayer;
    }
}
