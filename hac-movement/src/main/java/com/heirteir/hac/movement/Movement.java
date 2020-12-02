package com.heirteir.hac.movement;

import com.heirteir.hac.movement.dynamic.DynamicClassManager;
import com.heirteir.hac.util.dependency.plugin.DependencyPlugin;
import com.heirteir.hac.util.dependency.types.annotation.Github;
import com.heirteir.hac.util.dependency.types.annotation.Maven;
import lombok.Getter;

@Maven(groupId = "org.javassist", artifactId = "javassist", version = "3.27.0-GA")
@Github(fileName = "HAC.Core", pluginName = "HAC-Core", githubRepoRelativeURL = "heirteir-commits/HAC", spigotId = 75180)
@Getter
public final class Movement extends DependencyPlugin {
    private DynamicClassManager classManager;

    public Movement() {
        super("Movement");
    }

    @Override
    protected void enable() {
        if (this.classManager != null) {
            this.classManager.load();
        }
    }

    @Override
    protected void disable() {
        if (this.classManager != null) {
            this.classManager.unload();
        }
    }

    @Override
    protected void load() {
        this.classManager = new DynamicClassManager(this);
    }
}
