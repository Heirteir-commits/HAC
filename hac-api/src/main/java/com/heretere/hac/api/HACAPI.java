package com.heretere.hac.api;

import com.google.common.base.Preconditions;
import com.heretere.hac.api.concurrency.ThreadPool;
import com.heretere.hac.api.events.ASyncPacketEventManager;
import com.heretere.hac.api.events.types.packets.PacketReferences;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.api.player.HACPlayerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;
import java.util.logging.Level;

/**
 * API for HAC
 */
public final class HACAPI {
    private static HACAPI instance;

    /**
     * Gets the current instance of the API.
     *
     * @return The API instance.
     */
    public static HACAPI getInstance() {
        if (HACAPI.instance == null) {
            instance = new HACAPI();
        }

        return HACAPI.instance;
    }

    private final ASyncPacketEventManager eventManager;
    private final ThreadPool threadPool;
    private final HACPlayerList hacPlayerList;
    private final ErrorHandler errorHandler;
    private final PacketReferences packetReferences;

    private boolean loaded;

    private HACAPI() {
        Preconditions.checkState(HACAPI.instance == null, "There can only be one instance of HACAPI.");

        this.eventManager = new ASyncPacketEventManager();
        this.threadPool = new ThreadPool();
        this.hacPlayerList = new HACPlayerList();
        this.errorHandler = new ErrorHandler();
        this.packetReferences = new PacketReferences();

        this.loaded = true;
    }

    /**
     * This is used to insure the API is loaded before passing any information.
     * It's really just a sanity check.
     */
    private void checkLoaded() {
        Preconditions.checkState(this.loaded, "HACAPI not loaded.");
    }

    /**
     * Unloads the API this method is called in hac-core to ensure the API shuts things down during a disable.
     */
    public void unload() {
        this.checkLoaded();
        this.threadPool.unload();
        this.loaded = false;
    }

    /**
     * This is the global event manager responsible for passing events throughout HAC.
     *
     * @return Global async event manager for HAC.
     */
    public ASyncPacketEventManager getEventManager() {
        this.checkLoaded();
        return this.eventManager;
    }

    /**
     * This is the thread pool instance for HAC. This is used to multi-thread the events inside HAC to reduce load
     * on the main server thread.
     *
     * @return By Default a Cached Thread Pool.
     */
    public ThreadPool getThreadPool() {
        this.checkLoaded();
        return this.threadPool;
    }

    /**
     * Anytime a {@link HACPlayer} is created it is registered to this list. hac-core manages removing and adding players to the
     * list.
     *
     * @return All HACPlayer instances registered by HAC.
     */
    public HACPlayerList getHacPlayerList() {
        this.checkLoaded();
        return this.hacPlayerList;
    }

    /**
     * Anytime an error occurs inside the API it is sent to this {@link HACAPI.ErrorHandler}, errors outside the API should be handled
     * by it's respective plugin.
     *
     * @return The ErrorHandler for the API.
     */
    public ErrorHandler getErrorHandler() {
        return this.errorHandler;
    }

    public PacketReferences getPacketReferences() {
        this.checkLoaded();
        return this.packetReferences;
    }

    /**
     * This class is used to offload the error's inside the API to one place. By default hac-core overwrites the
     * error handler so error information can be outputted to a log.
     */
    public static final class ErrorHandler {
        private Consumer<Throwable> handler;

        private ErrorHandler() {
            Plugin plugin = JavaPlugin.getProvidingPlugin(ErrorHandler.class);
            this.handler = ex -> plugin.getLogger().log(Level.SEVERE, ex.getMessage(), ex);
        }

        /**
         * Gets the currently registered error handler for the API.
         *
         * @return Consumer currently used for error handling.
         */
        public Consumer<Throwable> getHandler() {
            return handler;
        }

        /**
         * Overwrites the error handler with a new consumer to handle errors.
         *
         * @param handler The new error handling consumer.
         */
        public void setHandler(Consumer<Throwable> handler) {
            this.handler = handler;
        }
    }
}
