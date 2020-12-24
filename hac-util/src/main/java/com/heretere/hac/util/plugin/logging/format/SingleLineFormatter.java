package com.heretere.hac.util.plugin.logging.format;

import org.jetbrains.annotations.NotNull;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.regex.Pattern;

/**
 * The type Single line formatter.
 */
public final class SingleLineFormatter extends Formatter {
    /**
     * This is used to remove ansi colors from the outputted text.
     */
    private static final @NotNull Pattern STRIP_COLOR = Pattern.compile("\\x1b\\[[0-9;]*m");

    @Override
    public @NotNull String format(final @NotNull LogRecord record) {
        String prefix = "[" + record.getLevel() + "] ";
        String message = STRIP_COLOR.matcher(formatMessage(record)).replaceAll("") + System.lineSeparator();
        String error = record.getThrown() == null ? "" : record.getThrown().getMessage() + System.lineSeparator();

        return prefix + message + error;
    }
}
