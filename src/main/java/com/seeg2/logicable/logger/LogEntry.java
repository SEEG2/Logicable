package com.seeg2.logicable.logger;

import com.seeg2.logicable.util.Time;

public class LogEntry {
    private String time;
    private long sysTimeMS;
    private LogEntryType type;
    private String message;

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
}
