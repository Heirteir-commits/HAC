package com.heretere.hac.core;

import com.heretere.hac.core.proxy.CoreVersionProxy;
import com.heretere.hac.annotations.plugin.Plugin;
import com.heretere.hac.util.proxy.AbstractProxyPlugin;

@Plugin(name = "HAC-Core", prefix = "-", version = "0.0.1")
public final class Core extends AbstractProxyPlugin<CoreVersionProxy> {

    public Core() {
        super("com.heretere.hac.core.proxy.versions", CoreVersionProxy.class);
    }

//    @Override
//    protected void enable() {
//
//    }
//
//    @Override
//    protected void disable() {
//
//    }

}
