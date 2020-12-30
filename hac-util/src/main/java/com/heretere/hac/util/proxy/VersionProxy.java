package com.heretere.hac.util.proxy;

public interface VersionProxy {
    /**
     * Should run any global proxy logic, then delegate to the load method.
     */
    void preLoad();

    /**
     * Should run any global proxy logic, then delegate to the unload method.
     */
    void preUnload();

    /**
     * Main loading logic should be handled by an nms proxy version.
     */
    void load();

    /**
     * Main unloading logic should be handed by an nms proxy version.
     */
    void unload();
}
