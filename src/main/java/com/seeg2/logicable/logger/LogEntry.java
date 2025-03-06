package com.seeg2.logicable.logger;

import com.seeg2.logicable.util.Time;

public class LogEntry {
    private final String time;
    private final long sysTimeMS;
    private final LogEntryType type;
    private final String message;

    public enum LogEntryType {
        INFO,
        DEBUG,
        WARNING,
        ERROR,
        CRITICAL
    }

    public LogEntry(String message, LogEntryType type) {
        this.message = message;
        this.type = type;
        this.time = Time.zonedDateTimeHHMMSS();
        this.sysTimeMS = System.currentTimeMillis();

        Logger.notifyListeners(this);
    }

    public String getText() {
        return "[" + time + "] " + message;
    }
    public LogEntryType getType() {
        return type;
    }
}
