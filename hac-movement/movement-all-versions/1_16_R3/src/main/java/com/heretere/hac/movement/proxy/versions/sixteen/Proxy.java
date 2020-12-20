package com.heretere.hac.movement.proxy.versions.sixteen;

import com.heretere.hac.movement.proxy.AbstractMovementVersionProxy;
import com.heretere.hac.movement.proxy.TestWorldHelper;
import com.heretere.hac.movement.proxy.versions.sixteen.helper.WorldHelper;
import com.heretere.hac.util.plugin.AbstractHACPlugin;

public class Proxy extends AbstractMovementVersionProxy {
    private final WorldHelper helper;

    public Proxy(AbstractHACPlugin parent) {
        this.helper = new WorldHelper();
    }


    @Override
    public TestWorldHelper getWorldHelper() {
        return this.helper;
    }

    @Override
    protected void load() {

    }

    @Override
    protected void unload() {

    }
}
