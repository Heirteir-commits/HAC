/*
 * MIT License
 *
 * Copyright (c) 2021 Justin Heflin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

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
