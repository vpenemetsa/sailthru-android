package com.sailthru.android.sdk.impl.logger;

/**
 * Created by Vijay Penemetsa on 5/22/14.
 *
 * Abstract Logger class that can be implemented to intercept log messages
 */
public abstract class Logger {

    public static final String BASE_TAG = "com.sailthru.";

    public enum LogLevel {
        NONE(0), BASIC(1), FULL(2);

        public final int value;

        private LogLevel(int value) {
            this.value = value;
        }
    }

    protected LogLevel logLevel;
    protected static boolean interceptLogs = false;

    public Logger() {
        logLevel = LogLevel.BASIC;
    }

    /**
     * Used to display debug log messages
     *
     * @param tag String
     * @param message String
     */
    protected abstract void d(LogLevel logLevel, String tag, String message);

    /**
     * Used to display warning log messages
     *
     * @param tag String
     * @param message String
     */
    protected abstract void w(LogLevel logLevel, String tag, String message);

    /**
     * Used to display error log messages
     *
     * @param tag String
     * @param message String
     */
    protected abstract void e(LogLevel logLevel, String tag, String message);
}