package com.heretere.hac.core;

import com.heretere.hac.api.HACAPI;
import com.heretere.hac.core.player.HACPlayerListUpdater;
import com.heretere.hac.core.proxy.CoreVersionProxy;
import com.heretere.hac.core.proxy.player.PlayerData;
import com.heretere.hac.core.proxy.player.PlayerDataFactory;
import com.heretere.hac.util.plugin.dependency.annotations.Maven;
import com.heretere.hac.util.plugin.dependency.relocation.annotations.Relocation;
import com.heretere.hac.util.proxy.ProxyPlugin;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.LogPrefix;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* Plugin */
@Plugin(name = "HAC-Core", version = "0.0.1")
@LogPrefix("-")
@ApiVersion(ApiVersion.Target.v1_13)

/* Dependencies */
@Maven(groupId = "org|bstats",
       artifactId = "bstats-bukkit",
       version = "1.7",
       repoUrl = "https://repo.codemc.org/repository/maven-public/")
@Maven(groupId = "com|flowpowered", artifactId = "flow-math", version = "1.0.3")
@Maven(groupId = "org|tomlj", artifactId = "tomlj", version = "1.0.0")
@Maven(groupId = "org|antlr", artifactId = "antlr4-runtime", version = "4.7.2")
@Maven(groupId = "org|apache|commons", artifactId = "commons-lang3", version = "3.11")
/* Relocations */
@Relocation(from = "org|bstats|bukkit", to = "com|heretere|hac|core|libs|bstats|bukkit")
@Relocation(from = "com|flowpowered|math", to = "com|heretere|hac|core|libs|math")
@Relocation(from = "org|tomlj", to = "com|heretere|hac|core|libs|tomlj")
@Relocation(from = "org|antlr", to = "com|heretere|hac|core|libs|antlr")
@Relocation(from = "org|apache|commons|lang3", to = "com|heretere|hac|core|libs|lang3")
public final class Core extends ProxyPlugin<CoreVersionProxy> {
    /**
     * The id for this plugin on bstats.
     *
     * @see <a href="https://bstats.org">http://bstats.org</a>
     */
    private static final int BSTATS_ID = 9648;

    /**
     * The HACAPI instance.
     */
    private final @NotNull HACAPI api;

    /**
     * The class responsible for updating the HACPlayerList in the api.
     */
    private @Nullable HACPlayerListUpdater hacPlayerListUpdater;
    /**
     * The factory responsible for creating new PlayerData instances for each HACPlayer.
     */
    private @Nullable PlayerDataFactory playerDataFactory;

    /**
     * Instantiates core.
     */
    public Core() {
        super("HAC", "Core", "com.heretere.hac.core.proxy.versions", CoreVersionProxy.class);
        this.api = new HACAPI(this);
    }

    @Override
    public void proxyLoad() {
        Bukkit.getServer()
              .getServicesManager()
              .register(HACAPI.class, this.api, this, ServicePriority.Normal);
    }

    @Override
    public void proxyEnable() {
        try {
            this.hacPlayerListUpdater = new HACPlayerListUpdater(this.api, this);
            this.playerDataFactory = new PlayerDataFactory(this.api);
        } catch (Exception e) {
            super.getLog()
                 .reportFatalError(() -> "HAC failed to start correctly. Please look at the latest.log to determine " +
                     "the issue.", true);
            return;
        }

        this.api.getConfigHandler().loadConfigClass(this);
        new Metrics(this, Core.BSTATS_ID);

        this.api.getErrorHandler().setHandler(ex -> {
            if (ex != null) {
                this.getLog().severe(ex);
            }
        });

        this.getLog().info(() -> "Registering player data builder.");
        this.api
            .getHacPlayerList()
            .getFactory()
            .registerDataBuilder(PlayerData.class, this.playerDataFactory);

        this.hacPlayerListUpdater.load();
        super.getProxy().preLoad();
    }

    @Override
    public void proxyDisable() {
        if (this.hacPlayerListUpdater == null || this.playerDataFactory == null) {
            return;
        }

        this.hacPlayerListUpdater.unload();
        super.getProxy().preUnload();

        this.getLog().info(() -> "Unregistering player data builder.");
        this.api.getHacPlayerList().getFactory().unregisterDataBuilder(PlayerData.class);

        this.api.unload();
    }
}
