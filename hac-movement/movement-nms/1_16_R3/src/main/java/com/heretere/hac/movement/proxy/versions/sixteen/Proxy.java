package com.heretere.hac.movement.proxy.versions.sixteen;

import com.heretere.hac.movement.proxy.MovementVersionProxy;
import com.heretere.hac.movement.proxy.simulation.HumanAccessor;
import com.heretere.hac.movement.proxy.versions.sixteen.simulation.HACEntityHumanFactory;
import com.heretere.hac.util.plugin.HACPlugin;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

/**
 * The type Proxy.
 */
public final class Proxy extends MovementVersionProxy {
    private final HACEntityHumanFactory factory = new HACEntityHumanFactory();

    /**
     * Instantiates a new Proxy.
     *
     * @param parent the parent
     */
    public Proxy(final @NotNull HACPlugin parent) {

    }

    @Override
    protected void load() {
        //not used yet
    }

    @Override
    protected void unload() {
        //not used yet
    }

    @Override public HumanAccessor createHumanAccessor(World world) {
        return this.factory.createHumanAccessor(world);
    }
}
