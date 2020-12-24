package com.heretere.hac.util.plugin.logging;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.heretere.hac.util.plugin.HACPlugin;
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

/**
 * This class adds some functionality to the plugin logger.
 */
public final class Log {
    /**
     * Whenever an error is reported to the log file it will wrap it to this many characters.
     */
    private static final int ERROR_WRAP = 60;
    /**
     * Adds padding to the border of a reported error.
     */
    private static final int ERROR_PADDING = 3;

    /**
     * The parent plugin reference.
     */
    private final @NotNull HACPlugin parent;

    /**
     * The logging file location.
     */
    private final @NotNull Path loggingFile;

    /**
     * The state handler.
     */
    private boolean open;

    /**
     * Instantiates a new Log.
     *
     * @param parent the parent
     */
    public Log(final @NotNull HACPlugin parent) {
        this.parent = parent;
        this.loggingFile = this.parent.getBaseDirectory()
                                      .resolve("logs")
                                      .resolve(parent.getName())
                                      .resolve("latest.log.txt");

        try {
            Files.createDirectories(this.loggingFile.getParent());
        } catch (IOException e) {
            this.severe(e);
        }
    }

    /**
     * Opens the logging file.
     */
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

    /**
     * Closes the logging file.
     */
    public void close() {
        Preconditions.checkState(this.open, "There is no log file to close.");

        for (Handler handler : this.parent.getLogger().getHandlers()) {
            handler.close();
        }

        this.open = false;
    }

    private Supplier<String> toLogMessage(final @NotNull Supplier<String> message) {
        return () -> ChatColor.stripColor(ChatColorAnsi.colorCodeToAnsi(this.parent.getPrefix() + message.get()));
    }

    private void checkState() {
        Preconditions.checkState(
            this.open,
            "Log file hasn't been created. " + "Please have your plugin extend DependencyPlugin "
                + "or call Log.open() before logging messages."
        );
    }

    /**
     * Info.
     *
     * @param message the message
     */
    public synchronized void info(final @NotNull Supplier<String> message) {
        this.checkState();

        this.parent.getLogger().info(this.toLogMessage(message));
    }

    /**
     * Severe.
     *
     * @param message the message
     */
    public synchronized void severe(final @NotNull Supplier<String> message) {
        this.checkState();

        this.parent.getLogger().severe(this.toLogMessage(message));
    }

    /**
     * Severe.
     *
     * @param exception the exception
     */
    public synchronized void severe(final @NotNull Throwable exception) {
        this.checkState();

        this.parent.getLogger().log(Level.SEVERE, exception, this.toLogMessage(exception::getMessage));
    }

    /**
     * Report fatal error.
     *
     * @param message  the message
     * @param shutdown the shutdown
     */
    public synchronized void reportFatalError(
        final @NotNull Supplier<String> message,
        final boolean shutdown
    ) {
        Iterable<String> header = Splitter.fixedLength(ERROR_WRAP).split(String.format(
            "'%s' ran into an error that has forced the plugin to stop." + " More information below:",
            this.parent.getName()
        ));
        Iterable<String> body = Splitter.fixedLength(ERROR_WRAP).split(ChatColor.stripColor(message.get()));
        String headTail = "&c" + StringUtils.repeat("=", ERROR_WRAP + ERROR_PADDING + 2);
        String headerTail = "&c|" + StringUtils.repeat("=", ERROR_WRAP + ERROR_PADDING) + "|";

        this.severe(() -> headTail);
        header.forEach(line -> this.severe(() -> "&c|&r" + StringUtils.center(
            line,
            ERROR_WRAP + ERROR_PADDING
        ) + "&c|"));
        this.severe(() -> headerTail);
        body.forEach(line -> this.severe(() -> "&c|&r" + StringUtils.center(line, ERROR_WRAP + ERROR_PADDING) + "&c|"));
        this.severe(() -> headTail);

        if (shutdown && Bukkit.getPluginManager().isPluginEnabled(this.parent)) {
            Bukkit.getScheduler().runTaskLater(this.parent, () -> {
                if (Bukkit.getPluginManager().isPluginEnabled(this.parent)) {
                    Bukkit.getPluginManager().disablePlugin(this.parent);
                }
            }, 0L);
        }
    }

    /**
     * Report fatal error.
     *
     * @param exception the exception
     * @param shutdown  the shutdown
     */
    public synchronized void reportFatalError(
        final @NotNull Throwable exception,
        final boolean shutdown
    ) {
        this.severe(exception);
        this.reportFatalError(exception::getMessage, shutdown);
    }

    private enum ChatColorAnsi {
        /**
         * Red chat color ansi.
         */
        RED("&c", "\u001b[31m"),
        /**
         * Default chat color ansi.
         */
        DEFAULT("&r", "\u001b[0m");

        /**
         * The minecraft color with & instead of the section symbol.
         */
        private final String colorCode;
        /**
         * The ansi replacement for the color code.
         */
        private final String ansiCode;

        ChatColorAnsi(
            final @NotNull String colorCode,
            final @NotNull String ansiCode
        ) {
            this.colorCode = colorCode;
            this.ansiCode = ansiCode;
        }

        private static @NotNull String colorCodeToAnsi(final @NotNull String input) {
            String output = input;

            for (ChatColorAnsi ansi : ChatColorAnsi.values()) {
                output = StringUtils.replace(output, ansi.colorCode, ansi.ansiCode);
            }

            output += ChatColorAnsi.DEFAULT.ansiCode;

            return output;
        }
    }
}
