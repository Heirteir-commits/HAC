package com.heirteir.hac.util.logging;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.heirteir.hac.util.dependency.plugin.DependencyPlugin;
import com.heirteir.hac.util.files.FilePaths;
import com.heirteir.hac.util.logging.format.SingleLineFormatter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;

@SuppressWarnings("ImmutableEnumChecker") //This is a Singleton so it won't be Immutable
public enum Log {
    INSTANCE;

    /* Plugin Parent */
    private final DependencyPlugin parent;

    /* Files */
    private final Path loggingFile;

    /* State Handler */
    private boolean open;


    Log() {
        this.parent = (DependencyPlugin) JavaPlugin.getProvidingPlugin(Log.class);
        this.loggingFile = FilePaths.INSTANCE.getPluginFolder().resolve("log").resolve(parent.getName()).resolve("latest.log.txt");

        try {
            Files.createDirectories(this.loggingFile.getParent());
        } catch (IOException e) {
            this.severe(e);
        }
    }

    public void open() {
        Preconditions.checkState(!this.open, "Log file already opened.");

        try {
            FileHandler fh = new FileHandler(this.loggingFile.toAbsolutePath().toString());
            fh.setFormatter(new SingleLineFormatter());
            this.parent.getLogger().addHandler(fh);
            this.open = true;
        } catch (IOException e) {
            this.severe(e);
        }
    }

    public void close() {
        Preconditions.checkState(this.open, "There is no log file to close.");

        for (Handler handler : this.parent.getLogger().getHandlers()) {
            handler.close();
        }

        this.open = false;
    }

    private String toLogMessage(String message) {
        Preconditions.checkState(this.open, "Log file hasn't been created please have your plugin extend DependencyPlugin or call Log.open() before logging messages.");
        return ChatColor.translateAlternateColorCodes('&', parent.getLoggerPrefix() + message);
    }

    public void info(String message) {
        this.parent.getLogger().info(this.toLogMessage(message));
    }

    public void severe(String message) {
        this.parent.getLogger().severe(this.toLogMessage(message));
    }

    public void severe(Throwable exception) {
        this.parent.getLogger().log(Level.SEVERE, exception.getMessage(), exception);
    }

    public void reportFatalError(String message) {
        int splitSize = 60;
        Iterable<String> header = Splitter.fixedLength(splitSize).split(String.format("'%s' ran into an error that has forced the plugin to stop. More information below:", this.parent.getName()));
        Iterable<String> body = Splitter.fixedLength(splitSize).split(ChatColor.stripColor(message));
        String headTail = StringUtils.repeat("=", splitSize + 8);
        String headerTail = "|" + StringUtils.repeat("=", splitSize + 6) + "|";

        this.severe(headTail);
        header.forEach(line -> this.severe("|" + StringUtils.center(line, splitSize + 6) + "|"));
        this.severe(headerTail);
        body.forEach(line -> this.severe("|" + StringUtils.center(line, splitSize + 6) + "|"));
        this.severe(headTail);

        Bukkit.getPluginManager().disablePlugin(this.parent);
    }

    public void reportFatalError(Throwable exception) {
        this.severe(exception);
        this.reportFatalError(exception.getMessage());
    }
}
