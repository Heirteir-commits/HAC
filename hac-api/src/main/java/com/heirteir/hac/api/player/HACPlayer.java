package com.heirteir.hac.api.player;

import com.heirteir.hac.api.player.builder.DataManager;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public final class HACPlayer {
    private final DataManager dataManager;
    private final Player bukkitPlayer;


    public HACPlayer(Player player) {
        this.dataManager = new DataManager();
        this.bukkitPlayer = player;
    }
}
