package com.heretere.hac.core;

import com.heretere.hac.core.proxy.CoreVersionProxy;
import com.heretere.hac.util.plugin.dependency.annotations.Maven;
import com.heretere.hac.util.plugin.dependency.relocation.annotations.Relocation;
import com.heretere.hac.util.proxy.AbstractProxyPlugin;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.LogPrefix;
import org.bukkit.plugin.java.annotation.plugin.Plugin;

/* Plugin */
@Plugin(name = "HAC-Core", version = "0.0.1")
@LogPrefix("-")
@ApiVersion(ApiVersion.Target.v1_13)

/* Dependencies */
@Maven(
        groupId = "org.bstats",
        artifactId = "bstats-bukkit",
        version = "1.7",
        repoUrl = "https://repo.codemc.org/repository/maven-public/"
)
@Maven(
        groupId = "com.flowpowered",
        artifactId = "flow-math",
        version = "1.0.3"
)
@Relocation(from = "org|bstats|bukkit", to = "com|heretere|hac|core|libs|bstats|bukkit")
@Relocation(from = "com|flowpowered|math", to = "com|heretere|hac|core|libs|math")
public final class Core extends AbstractProxyPlugin<CoreVersionProxy> {
    /**
     * The id for this plugin on bstats.
     *
     * @see <a href="https://bstats.org">http://bstats.org</a>
     */
    private static final int BSTATS_ID = 9648;

    /**
     * Instantiates core.
     */
    public Core() {
        super(
                "HAC",
                "Core",
                "com.heretere.hac.core.proxy.versions",
                CoreVersionProxy.class
        );
    }

    @Override
    public void proxyLoad() {
        //none
    }

    @Override
    public void proxyEnable() {
        new Metrics(this, Core.BSTATS_ID);
        super.getProxy().baseLoad();
    }

    @Override
    public void proxyDisable() {
        super.getProxy().baseUnload();
    }

}
