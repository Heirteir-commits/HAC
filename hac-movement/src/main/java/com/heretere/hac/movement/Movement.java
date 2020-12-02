package com.heretere.hac.movement;

import com.heretere.hac.api.API;
import com.heretere.hac.api.util.reflections.version.ServerVersion;
import com.heretere.hac.movement.dynamic.DynamicClassManager;
import com.heretere.hac.movement.player.data.Simulator;
import com.heretere.hac.movement.player.data.SimulatorBuilder;
import com.heretere.hac.movement.util.reflections.FireworksEntityHelper;
import com.heretere.hac.util.dependency.plugin.DependencyPlugin;
import com.heretere.hac.util.dependency.types.annotation.Github;
import com.heretere.hac.util.dependency.types.annotation.Maven;
import lombok.Getter;

@Getter
@Maven(groupId = "net.bytebuddy", artifactId = "byte-buddy", version = "1.10.18")
@Github(fileName = "HAC.Core", pluginName = "HAC-Core", githubRepoRelativeURL = "heretere/hac", spigotId = 75180)
public final class Movement extends DependencyPlugin {
    private DynamicClassManager classManager;
    private SimulatorBuilder simulatorBuilder;

    public Movement() {
        super("Movement");
    }

    @Override
    protected void enable() {
        this.classManager.load();

        if (API.INSTANCE.getReflections().getVersion().greaterThanOrEqual(ServerVersion.ELEVEN_R1)) {
            super.getLog().info(String.format("Registering Reflections Helper '%s'.", FireworksEntityHelper.class));
            API.INSTANCE.getReflections().getHelpers().registerHelper(FireworksEntityHelper.class, new FireworksEntityHelper(this));
        }

        super.getLog().info(String.format("Registering Data Builder '%s'", Simulator.class));
        API.INSTANCE.getHacPlayerList().getBuilder().registerDataBuilder(Simulator.class, this.simulatorBuilder);
    }

    @Override
    protected void disable() {
        if (this.classManager != null) {
            this.classManager.unload();
        }

        if (API.INSTANCE.getReflections().getVersion().greaterThanOrEqual(ServerVersion.ELEVEN_R1)) {
            super.getLog().info(String.format("Unregistering Reflections Helper '%s'.", FireworksEntityHelper.class));
            API.INSTANCE.getReflections().getHelpers().unregisterHelper(FireworksEntityHelper.class);
        }


        super.getLog().info(String.format("unregistering Data Builder '%s'", Simulator.class));
        API.INSTANCE.getHacPlayerList().getBuilder().unregisterDataBuilder(Simulator.class);
    }

    @Override
    protected void load() {
        this.classManager = new DynamicClassManager(this);
        this.simulatorBuilder = new SimulatorBuilder(this);
    }
}
