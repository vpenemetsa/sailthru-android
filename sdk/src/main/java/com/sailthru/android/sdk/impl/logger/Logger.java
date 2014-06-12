package com.sailthru.android.sdk.impl.logger;

/**
 * Created by Vijay Penemetsa on 5/22/14.
 *
 * Abstract Logger class that can be implemented to intercept log messages
 */
public abstract class Logger {

    public static final String BASE_TAG = "com.sailthru.";

    public enum LogLevel {
        NONE, FULL, BASIC
    }

    protected LogLevel logLevel;

    protected static boolean interceptLogs = false;

    public Logger() {
    }

    public void setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
    }

    /**
     * Used to display debug log messages
     *
     * @param tag String
     * @param message String
     */
    protected abstract void d(String tag, String message);

    /**
     * Used to display warning log messages
     *
     * @param tag String
     * @param message String
     */
    protected abstract void w(String tag, String message);

    /**
     * Used to display error log messages
     *
     * @param tag String
     * @param message String
     */
    protected abstract void e(String tag, String message);
}