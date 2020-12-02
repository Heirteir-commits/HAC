package com.heirteir.hac.api.player;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.heirteir.hac.api.player.builder.AbstractDataBuilder;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public final class HACPlayerBuilder {
    private final HACPlayerList playerList;

    private final Map<Class<?>, AbstractDataBuilder<?>> builders;

    public HACPlayerBuilder(HACPlayerList playerList) {
        this.playerList = playerList;
        this.builders = Maps.newLinkedHashMap();
    }

    public void registerDataBuilder(@NotNull Class<?> clazz, @NotNull AbstractDataBuilder<?> builder) {
        this.builders.put(clazz, builder);
        this.playerList.getAll().forEach(player -> player.getDataManager().addData(clazz, builder.build(player)));
        builder.registerUpdaters();
    }

    public void unregisterDataBuilder(@NotNull Class<?> clazz) {
        AbstractDataBuilder<?> builder = this.builders.remove(clazz);

        Preconditions.checkNotNull(builder, String.format("No builder registered of class '%s'.", clazz));
        builder.removeUpdaters();

        this.playerList.getAll()
                .forEach(player -> player.getDataManager().removeData(clazz));
    }

    public HACPlayer build(Player player) {
        HACPlayer hacPlayer = new HACPlayer(player);
        this.builders
                .forEach((key, value) -> hacPlayer.getDataManager().addData(key, value.build(hacPlayer)));
        return hacPlayer;
    }
}
