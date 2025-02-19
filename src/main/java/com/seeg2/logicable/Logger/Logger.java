package com.seeg2.logicable.Logger;

import java.util.ArrayList;

public class Logger {
    private static ArrayList<LogEntry> logEntries;

    public static void createEntry(String message, LogEntry.LogEntryType type) {
        logEntries.add(new LogEntry(message, type));
    }

    public static void info(String message) {
        logEntries.add(new LogEntry(message, LogEntry.LogEntryType.INFO));
    }
    public static void debug(String message) {
        logEntries.add(new LogEntry(message, LogEntry.LogEntryType.DEBUG));
    }

    public static void warning(String message) {
        logEntries.add(new LogEntry(message, LogEntry.LogEntryType.WARNING));
    }

    public static void error(String message) {
        logEntries.add(new LogEntry(message, LogEntry.LogEntryType.ERROR));
    }

    public static void critical(String message) {
        logEntries.add(new LogEntry(message, LogEntry.LogEntryType.CRITICAL));
    }
}
