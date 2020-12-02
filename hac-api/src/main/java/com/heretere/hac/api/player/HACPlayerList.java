package com.heretere.hac.api.player;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class HACPlayerList {

    @Getter
    private final HACPlayerBuilder builder;
    private final Map<UUID, HACPlayer> players;

    public HACPlayerList() {
        this.builder = new HACPlayerBuilder(this);
        this.players = Maps.newHashMap();
    }

    public HACPlayer getPlayer(@NotNull UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);

        Preconditions.checkNotNull(player, String.format("No player online with uuid '%s'", uuid));

        return this.getPlayer(player);
    }

    public HACPlayer getPlayer(@NotNull Player player) {
        return this.players.computeIfAbsent(player.getUniqueId(), id -> this.builder.build(player));
    }

    public HACPlayer removePlayer(@NotNull UUID uuid) {
        return this.players.remove(uuid);
    }

    public HACPlayer removePlayer(@NotNull Player player) {
        return this.removePlayer(player.getUniqueId());
    }

    public Set<HACPlayer> getAll() {
        return Sets.newHashSet(this.players.values());
    }
}
