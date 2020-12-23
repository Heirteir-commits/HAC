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
    private static final Pattern STRIP_COLOR = Pattern.compile("\u001b\\[[^m]*+");

    /**
     * Format LogRecord to string
     * @param record the LogRecord
     * @return the string formatted
     */
    @Override
    public String format(@NotNull final LogRecord record) {
        return STRIP_COLOR.matcher("[" + record.getLevel() + "] " +
                                       formatMessage(record) + "\n" +
                                       (record.getThrown() != null
                                           ? record.getThrown().getMessage() + "\n"
                                           : "")).replaceAll("");
    }
}
