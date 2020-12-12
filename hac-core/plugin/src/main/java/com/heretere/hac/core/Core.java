package com.heretere.hac.core;

import com.heretere.hac.core.proxy.CoreVersionProxy;
import com.heretere.hac.util.proxy.AbstractProxyPlugin;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.LogPrefix;
import org.bukkit.plugin.java.annotation.plugin.Plugin;

@Plugin(name = "HAC-Core", version = "0.0.1")
@LogPrefix("-")
@ApiVersion(ApiVersion.Target.v1_13)

public final class Core extends AbstractProxyPlugin<CoreVersionProxy> {

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
        super.getProxy().baseLoad();
    }

    @Override
    public void proxyDisable() {
        super.getProxy().baseUnload();
    }

}
