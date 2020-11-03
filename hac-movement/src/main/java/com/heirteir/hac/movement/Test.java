package com.heirteir.hac.movement;

import com.heirteir.hac.util.dependency.plugin.DependencyPlugin;
import com.heirteir.hac.util.dependency.types.annotation.Maven;

@Maven(groupId = "org.projectlombok", artifactId = "lombok", version = "1.18.16")
public class Test extends DependencyPlugin {
    public Test() {
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
