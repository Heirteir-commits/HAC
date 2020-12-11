package com.heretere.hac.util.plugin.logging;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.heretere.hac.util.plugin.AbstractHACPlugin;
import com.heretere.hac.util.plugin.logging.format.SingleLineFormatter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;

public final class Log {
    /* Plugin Parent */
    private final AbstractHACPlugin parent;

    /* Files */
    private final Path loggingFile;

    /* State Handler */
    private boolean open;

    public Log(AbstractHACPlugin parent) {
        this.parent = parent;
        this.loggingFile = this.parent.getBaseDirectory().resolve("logs").resolve(parent.getName()).resolve("latest.log.txt");

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
        return ChatColor.stripColor(ChatColorAnsi.colorCodeToAnsi(parent.getPrefix() + message));
    }

    private void checkState() {
        Preconditions.checkState(this.open, "Log file hasn't been created please have your plugin extend DependencyPlugin or call Log.open() before logging messages.");
    }

    public void info(@NotNull Supplier<String> message) {
        this.checkState();

        this.parent.getLogger().info(message);
    }

    public void severe(@NotNull Supplier<String> message) {
        this.checkState();

        this.parent.getLogger().severe(() -> this.toLogMessage(message.get()));
    }

    public void severe(@NotNull Throwable exception) {
        this.checkState();

        this.parent.getLogger().log(Level.SEVERE, exception, exception::getMessage);
    }

    public void reportFatalError(@NotNull Supplier<String> message, boolean shutdown) {
        int splitSize = 60;
        int padding = 6;
        Iterable<String> header = Splitter.fixedLength(splitSize).split(String.format("'%s' ran into an error that has forced the plugin to stop. More information below:", this.parent.getName()));
        Iterable<String> body = Splitter.fixedLength(splitSize).split(ChatColor.stripColor(message.get()));
        String headTail = "&c" + StringUtils.repeat("=", splitSize + padding + 2);
        String headerTail = "&c|" + StringUtils.repeat("=", splitSize + padding) + "|";

        this.severe(() -> headTail);
        header.forEach(line -> this.severe(() -> "&c|&r" + StringUtils.center(line, splitSize + padding) + "&c|"));
        this.severe(() -> headerTail);
        body.forEach(line -> this.severe(() -> "&c|&r" + StringUtils.center(line, splitSize + padding) + "&c|"));
        this.severe(() -> headTail);

        if (shutdown && Bukkit.getPluginManager().isPluginEnabled(this.parent)) {
            Bukkit.getScheduler().runTaskLater(this.parent, () -> {
                if (Bukkit.getPluginManager().isPluginEnabled(this.parent)) {
                    Bukkit.getPluginManager().disablePlugin(this.parent);
                }
            }, 0L);
        }
    }

    public void reportFatalError(@NotNull Throwable exception, boolean shutdown) {
        this.severe(exception);
        this.reportFatalError(exception::getMessage, shutdown);
    }

    private enum ChatColorAnsi {
        RED("&c", "\u001b[31m"),
        DEFAULT("&r", "\u001b[0m");

        private final String colorCode;
        private final String ansiCode;

        ChatColorAnsi(String colorCode, String ansiCode) {
            this.colorCode = colorCode;
            this.ansiCode = ansiCode;
        }

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
