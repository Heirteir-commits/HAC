package com.heretere.hac.util.plugin.logging.format;

import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.regex.Pattern;

/**
 * The type Single line formatter.
 */
public final class SingleLineFormatter extends Formatter {
    /**
     * How many spaces to the stacktrace if an error occurs.
     */
    private static final int INDENT = 4;

    /**
     * This is used to remove ansi colors from the outputted text.
     */
    private static final @NotNull Pattern STRIP_COLOR = Pattern.compile("\\x1b\\[[0-9;]*m");

    @Override
    public @NotNull String format(final @NotNull LogRecord record) {
        StringBuilder message = new StringBuilder();
        message.append("[").append(record.getLevel()).append("] ");


        if (record.getThrown() == null) {
            message.append(STRIP_COLOR.matcher(formatMessage(record)).replaceAll(""))
                   .append(System.lineSeparator());
        } else {
            message.append(record.getThrown().getClass()).append(": ")
                   .append(record.getThrown().getMessage())
                   .append(System.lineSeparator());

            for (StackTraceElement element : record.getThrown().getStackTrace()) {
                message.append(StringUtils.repeat(" ", INDENT)).append("at: ").append(element.toString())
                       .append(System.lineSeparator());
            }
        }

        return message.toString();
    }
}
