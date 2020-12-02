package com.heretere.hac.core;

import com.google.common.collect.Queues;
import com.heretere.hac.api.API;
import com.heretere.hac.api.util.reflections.version.ServerVersion;
import com.heretere.hac.core.packets.ChannelInjector;
import com.heretere.hac.core.player.HACPlayerListUpdater;
import com.heretere.hac.core.player.data.location.PlayerData;
import com.heretere.hac.core.player.data.location.PlayerDataBuilder;
import com.heretere.hac.core.util.reflections.helper.AbstractCoreHelper;
import com.heretere.hac.core.util.reflections.helper.BoundingBoxHelper;
import com.heretere.hac.core.util.reflections.helper.EntityHelper;
import com.heretere.hac.core.util.reflections.helper.WorldHelper;
import com.heretere.hac.util.dependency.DependencyUtils;
import com.heretere.hac.util.dependency.plugin.DependencyPlugin;
import com.heretere.hac.util.dependency.types.GithubDependency;
import com.heretere.hac.util.dependency.types.annotation.Maven;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;
import java.util.ArrayDeque;

@Getter
@Maven(groupId = "com.flowpowered", artifactId = "flow-math", version = "1.0.3")
public final class Core extends DependencyPlugin {
    private ChannelInjector channelInjector;
    private HACPlayerListUpdater playerListUpdater;

    /* Data Builders */
    private PlayerDataBuilder playerDataBuilder;

    @Getter(AccessLevel.NONE)
    private final ArrayDeque<AbstractCoreHelper<?>> helpers;

    public Core() {
        super("Core");
        this.helpers = Queues.newArrayDeque();
    }

    Core(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
        this.helpers = Queues.newArrayDeque();
    }

    @Override
    protected void enable() {
        if (API.INSTANCE.getReflections().getVersion().equals(ServerVersion.INVALID)) {
            super.getLog().reportFatalError("HAC only supports [1.8 - 1.16.3].");
            return;
        }

        super.getLog().info("Registering default async error handler.");
        API.INSTANCE.getThreadPool().setDefaultErrorHandler((msg, ex) -> {
            if (ex != null) {
                super.getLog().severe(ex);
            }
        });

        this.helpers.add(new EntityHelper(this));
        this.helpers.add(new BoundingBoxHelper(this));
        this.helpers.add(new WorldHelper(this));

        this.helpers.forEach(AbstractCoreHelper::load);

        super.getLog().info("Booting up the Channel Injector.");
        this.channelInjector = new ChannelInjector(this);

        super.getLog().info("Booting up the HAC Player List.");
        this.playerListUpdater = new HACPlayerListUpdater(this);

        this.playerDataBuilder = new PlayerDataBuilder();
        super.getLog().info(String.format("Registering Data Builder '%s'", PlayerData.class));
        API.INSTANCE.getHacPlayerList().getBuilder().registerDataBuilder(PlayerData.class, this.playerDataBuilder);
    }

    @Override
    protected void disable() {
        if (!API.INSTANCE.getReflections().getVersion().equals(ServerVersion.INVALID)) {
            super.getLog().info(String.format("Unregistering Data Builder '%s'", PlayerData.class));
            API.INSTANCE.getHacPlayerList().getBuilder().unregisterDataBuilder(PlayerData.class);

            super.getLog().info("Shutting down the HAC Player List.");
            this.playerListUpdater.unload();
            super.getLog().info("Shutting down the Channel Injector.");
            this.channelInjector.unload();

            this.helpers
                    .descendingIterator()
                    .forEachRemaining(AbstractCoreHelper::unload);

            super.getLog().info("Closing Thread Pool");
            API.INSTANCE.getThreadPool().unload();
        }
    }

    @Override
    protected void load() {
        super.getLog().info("Checking for update...");
        DependencyUtils.loadDependency(this, new GithubDependency(this, "HAC.Core", "HAC-Core", "heretere/hac", 75180, true, false)); //check for update
    }
}
