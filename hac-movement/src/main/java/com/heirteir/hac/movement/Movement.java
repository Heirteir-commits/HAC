package com.heirteir.hac.movement;

import com.heirteir.hac.util.dependency.plugin.DependencyPlugin;
import com.heirteir.hac.util.dependency.types.annotation.Maven;

@Maven(groupId = "org.javassist", artifactId = "javassist", version = "3.27.0-GA")
public class Movement extends DependencyPlugin {
    protected Movement() {
        super("Movement");
    }

    @Override
    protected void enable() {

    }

    @Override
    protected void disable() {

    }

    @Override
    protected void load() {

    }
}
