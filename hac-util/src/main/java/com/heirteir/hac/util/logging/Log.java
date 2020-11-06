package com.heirteir.hac.util.logging;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.heirteir.hac.util.dependency.plugin.DependencyPlugin;
import com.heirteir.hac.util.files.FilePaths;
import com.heirteir.hac.util.logging.format.SingleLineFormatter;
import lombok.RequiredArgsConstructor;
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

@SuppressWarnings("ImmutableEnumChecker") //This is a Singleton so be quiet
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
            fh.setEncoding("UTF-8");
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
        return ChatColor.stripColor(ChatColorAnsi.colorCodeToAnsi(parent.getLoggerPrefix() + message));
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
        int padding = 6;
        Iterable<String> header = Splitter.fixedLength(splitSize).split(String.format("'%s' ran into an error that has forced the plugin to stop. More information below:", this.parent.getName()));
        Iterable<String> body = Splitter.fixedLength(splitSize).split(ChatColor.stripColor(message));
        String headTail = "&c" + StringUtils.repeat("=", splitSize + padding + 2);
        String headerTail = "&c|" + StringUtils.repeat("=", splitSize + padding) + "|";

        this.severe(headTail);
        header.forEach(line -> this.severe("&c|&r" + StringUtils.center(line, splitSize + padding) + "&c|"));
        this.severe(headerTail);
        body.forEach(line -> this.severe("&c|&r" + StringUtils.center(line, splitSize + padding) + "&c|"));
        this.severe(headTail);

        if (Bukkit.getPluginManager().isPluginEnabled(this.parent))
            Bukkit.getPluginManager().disablePlugin(this.parent);
    }

    public void reportFatalError(Throwable exception) {
        this.severe(exception);
        this.reportFatalError(exception.getMessage());
    }

    @RequiredArgsConstructor
    private enum ChatColorAnsi {
        RED("&c", "\u001b[31m"),
        DEFAULT("&r", "\u001b[0m");

        private final String colorCode;
        private final String ansiCode;

        private static String colorCodeToAnsi(String input) {
            String output = input;

            for (ChatColorAnsi ansi : ChatColorAnsi.values()) {
                output = StringUtils.replace(output, ansi.colorCode, ansi.ansiCode);
            }

            output += ChatColorAnsi.DEFAULT.ansiCode;

            return output;
        }
    }
}
