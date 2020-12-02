package com.heirteir.hac.util.logging.format;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.regex.Pattern;

public class SingleLineFormatter extends Formatter {
    private final Pattern stripColor;

    public SingleLineFormatter() {
        this.stripColor = Pattern.compile("\u001b\\[[^m]*+");
    }

    @Override
    public String format(LogRecord record) {
        return this.stripColor.matcher("[" + record.getLevel() + "] " +
                formatMessage(record) + "\n" +
                (record.getThrown() != null ? record.getThrown().getMessage() + "\n" : ""))
                .replaceAll("");
    }
}