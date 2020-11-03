package com.heirteir.hac.util.logging.format;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class SingleLineFormatter extends Formatter {
    @Override
    public String format(LogRecord record) {
        return "[" + record.getLevel() + "] " +
                formatMessage(record) + "\n" +
                (record.getThrown() != null ? record.getThrown().getMessage() + "\n" : "");
    }
}