package com.heirteir.hac.movement;

import com.heirteir.hac.api.API;
import com.heirteir.hac.movement.dynamic.DynamicClassManager;
import com.heirteir.hac.movement.player.data.Simulator;
import com.heirteir.hac.movement.player.data.SimulatorBuilder;
import com.heirteir.hac.util.dependency.plugin.DependencyPlugin;
import com.heirteir.hac.util.dependency.types.annotation.Github;
import com.heirteir.hac.util.dependency.types.annotation.Maven;
import lombok.Getter;

@Getter
@Maven(groupId = "org.javassist", artifactId = "javassist", version = "3.27.0-GA")
@Github(fileName = "HAC.Core", pluginName = "HAC-Core", githubRepoRelativeURL = "heirteir-commits/HAC", spigotId = 75180)
public final class Movement extends DependencyPlugin {
    private DynamicClassManager classManager;
    private SimulatorBuilder simulatorBuilder;

    public Movement() {
        super("Movement");
    }

    @Override
    protected void enable() {
        this.classManager.load();

        super.getLog().info(String.format("Registering Data Builder '%s'", Simulator.class));
        API.INSTANCE.getHacPlayerList().getBuilder().registerDataBuilder(Simulator.class, this.simulatorBuilder);
    }

    @Override
    protected void disable() {
        this.classManager.unload();
        super.getLog().info(String.format("unregistering Data Builder '%s'", Simulator.class));
        API.INSTANCE.getHacPlayerList().getBuilder().unregisterDataBuilder(Simulator.class);
    }

    @Override
    protected void load() {
        this.classManager = new DynamicClassManager(this);
        this.simulatorBuilder = new SimulatorBuilder(this);
    }
}
