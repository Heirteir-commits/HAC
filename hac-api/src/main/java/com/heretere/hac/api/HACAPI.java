package com.heretere.hac.api;

import com.google.common.base.Preconditions;
import com.heretere.hac.api.concurrency.ThreadPool;
import com.heretere.hac.api.config.HACConfigHandler;
import com.heretere.hac.api.events.AsyncPacketEventManager;
import com.heretere.hac.api.events.packets.PacketReferences;
import com.heretere.hac.api.player.HACPlayerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.logging.Level;

/**
 * API for HAC.
 */
public final class HACAPI {
    /**
     * The singleton instance.
     */
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

    /**
     * The Config Handler instance.
     */
    private final HACConfigHandler configHandler;
    /**
     * The Event Manager instance.
     */
    private final AsyncPacketEventManager eventManager;
    /**
     * The ThreadPool instance.
     */
    private final ThreadPool threadPool;
    /**
     * The player list instance.
     */
    private final HACPlayerList hacPlayerList;
    /**
     * The error handler instance.
     */
    private final ErrorHandler errorHandler;
    /**
     * The packet references instance.
     */
    private final PacketReferences packetReferences;

    /**
     * Whether or not the api has been loaded.
     */
    private boolean loaded;

    private HACAPI() {
        Preconditions.checkState(HACAPI.instance == null, "There can only be one instance of HACAPI.");

        this.configHandler = new HACConfigHandler(this);
        this.eventManager = new AsyncPacketEventManager();
        this.threadPool = new ThreadPool();
        this.hacPlayerList = new HACPlayerList(this);
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
        this.configHandler.unload();
        this.threadPool.unload();
        this.loaded = false;
    }

    /**
     * Gets config handler.
     *
     * @return the config handler
     */
    public HACConfigHandler getConfigHandler() {
        this.checkLoaded();
        return this.configHandler;
    }

    /**
     * This is the global event manager responsible for passing events throughout HAC.
     *
     * @return Global async event manager for HAC.
     */
    public AsyncPacketEventManager getEventManager() {
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
     * Anytime a {@link com.heretere.hac.api.player.HACPlayer} is created it is registered to this list.
     * hac-core manages removing and adding players to the list.
     *
     * @return All HACPlayer instances registered by HAC.
     */
    public HACPlayerList getHacPlayerList() {
        this.checkLoaded();
        return this.hacPlayerList;
    }

    /**
     * Anytime an error occurs inside the API it is sent to this {@link HACAPI.ErrorHandler},
     * errors outside the API should be handled by it's respective plugin.
     *
     * @return The ErrorHandler for the API.
     */
    public ErrorHandler getErrorHandler() {
        return this.errorHandler;
    }

    /**
     * Gets packet references.
     *
     * @return the packet references
     */
    public PacketReferences getPacketReferences() {
        this.checkLoaded();
        return this.packetReferences;
    }

    /**
     * This class is used to offload the error's inside the API to one place. By default hac-core overwrites the
     * error handler so error information can be outputted to a log.
     */
    public static final class ErrorHandler {
        /**
         * The handler that errors are passed to.
         */
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
        public void setHandler(@NotNull final Consumer<Throwable> handler) {
            this.handler = handler;
        }
    }
}
