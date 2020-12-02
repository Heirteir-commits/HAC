package com.heretere.hac.core;

import com.heretere.hac.core.implementation.CoreImplementation;
import org.bukkit.plugin.java.JavaPlugin;

public class Core extends JavaPlugin {
    private final CoreImplementation coreImplementation;

    public Core() {
        this.coreImplementation = new CoreImplementation();
    }

    @Override
    public void onEnable() {
        super.onEnable();

        this.coreImplementation.load();
        System.out.println("test");
    }
}
