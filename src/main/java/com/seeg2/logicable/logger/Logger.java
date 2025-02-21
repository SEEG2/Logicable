package com.seeg2.logicable.logger;

import java.util.ArrayList;

public class Logger {
    private static ArrayList<LogEntry> logEntries = new ArrayList<>();
    private static ArrayList<LogEventListener> listeners = new ArrayList<>();

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

    public static String getLogAsText() {
        StringBuilder text = new StringBuilder();
        for (LogEntry logEntry : logEntries) {
            text.append(logEntry.getText());
            text.append('\n');
        }

        return text.toString();
    }

    public static ArrayList<LogEntry> getLogEntries() {
        return logEntries;
    }

    public static void clear() {
        logEntries.clear();
    }

    public static void subscribe(LogEventListener listener) {
        listeners.add(listener);
    }

    public static void unsubscribe(LogEventListener listener) {
        listeners.remove(listener);
    }

    public static void notifyListeners(LogEntry logEntry) {
        for (LogEventListener listener : listeners) {
            listener.onLogEntryAdded(logEntry);
        }
    }

    public static String getColorForEntry(LogEntry logEntry) {
        switch (logEntry.getType()) {
            default -> {
                return "#000000";
            } case DEBUG -> {
                return "#7b26a3";
            } case WARNING -> {
                return "#ffdd00";
            } case ERROR -> {
                return "#ff0000";
            } case CRITICAL -> {
                return "#780c00";
            }
        }
    }
}
