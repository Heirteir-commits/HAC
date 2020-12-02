package com.heirteir.hac.core;

import com.heirteir.hac.api.API;
import com.heirteir.hac.api.util.reflections.version.ServerVersion;
import com.heirteir.hac.core.packets.ChannelInjector;
import com.heirteir.hac.core.player.HACPlayerListUpdater;
import com.heirteir.hac.core.player.data.location.PlayerData;
import com.heirteir.hac.core.player.data.location.PlayerDataBuilder;
import com.heirteir.hac.core.util.reflections.helper.PlayerHelper;
import com.heirteir.hac.util.dependency.plugin.DependencyPlugin;
import com.heirteir.hac.util.dependency.types.annotation.Maven;
import com.heirteir.hac.util.logging.Log;
import lombok.Getter;

@Getter
@Maven(groupId = "com.flowpowered", artifactId = "flow-math", version = "1.0.3")
public final class Core extends DependencyPlugin {
    private ChannelInjector channelInjector;
    private HACPlayerListUpdater playerListUpdater;

    /* Data Builders */
    private PlayerDataBuilder playerDataBuilder;

    public Core() {
        super("Core");
    }

    @Override
    protected void enable() {
        if (API.INSTANCE.getReflections().getVersion().equals(ServerVersion.INVALID)) {
            Log.INSTANCE.reportFatalError("HAC only supports [1.8 - 1.16.3].");
            return;
        }

        Log.INSTANCE.info(String.format("Registering Reflections Helper '%s'.", PlayerHelper.class));
        API.INSTANCE.getReflections().getHelpers().registerHelper(PlayerHelper.class, new PlayerHelper(API.INSTANCE.getReflections()));

        Log.INSTANCE.info("Booting up the Channel Injector.");
        this.channelInjector = new ChannelInjector();

        Log.INSTANCE.info("Booting up the HAC Player List.");
        this.playerListUpdater = new HACPlayerListUpdater(this);

        this.playerDataBuilder = new PlayerDataBuilder();
        Log.INSTANCE.info(String.format("Registering Data Builder '%s'", PlayerData.class));
        API.INSTANCE.getHacPlayerList().getBuilder().registerDataBuilder(PlayerData.class, this.playerDataBuilder);
    }

    @Override
    protected void disable() {
        if (!API.INSTANCE.getReflections().getVersion().equals(ServerVersion.INVALID)) {
            Log.INSTANCE.info(String.format("Unregistering Data Builder '%s'", PlayerData.class));
            API.INSTANCE.getHacPlayerList().getBuilder().unregisterDataBuilder(PlayerData.class);

            Log.INSTANCE.info("Shutting down the HAC Player List.");
            this.playerListUpdater.unload();
            Log.INSTANCE.info("Shutting down the Channel Injector.");
            this.channelInjector.unload();

            Log.INSTANCE.info(String.format("Unregistering Reflections Helper '%s'.", PlayerHelper.class));
            API.INSTANCE.getReflections().getHelpers().unregisterHelper(PlayerHelper.class);
        }
    }

    @Override
    protected void load() {

    }
}
