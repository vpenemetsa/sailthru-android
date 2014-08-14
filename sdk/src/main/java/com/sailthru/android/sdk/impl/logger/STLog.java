package com.sailthru.android.sdk.impl.logger;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

/**
 * Created by Vijay Penemetsa on 6/11/14.
 *
 * Default implementation of {@link com.sailthru.android.sdk.impl.logger.Logger}
 */
public class STLog extends Logger {

    static Logger logger;
    static STLog STLog;

    public STLog() {
        super();
    }

    public static STLog getInstance() {
        if (STLog == null) {
            STLog = new STLog();
        }

        return STLog;
    }

    /**
     * Used to set an external logger. All messages are redirected to this logger if set
     *
     * @param externalLogger {@link com.sailthru.android.sdk.impl.logger.Logger}
     */
    public static void setExternalLogger(Logger externalLogger) {
        if (externalLogger != null) {
            logger = externalLogger;
            interceptLogs = true;
        }
    }

    /**
     * Sets log level
     *
     * @param logLevel {@link com.sailthru.android.sdk.impl.logger.Logger.LogLevel}
     */
    public void setLogLevel(LogLevel logLevel) {
        if (logLevel == null) {
            this.logLevel = LogLevel.NONE;
        } else {
            this.logLevel = logLevel;
        }
    }

    public static Logger getLogger() {
        return logger;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void d(final LogLevel logLevel, final String tag, final String message) {
        if (checkLogLevel(logLevel)) {
            if (interceptLogs && logger != null) {
                Handler handler = new Handler(Looper.getMainLooper());
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        logger.d(logLevel, BASE_TAG + tag, message);
                    }
                };
                handler.post(runnable);
            } else {
                Log.d(BASE_TAG + tag, message);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void w(final LogLevel logLevel, final String tag, final String message) {
        if (checkLogLevel(logLevel)) {
            if (interceptLogs && logger != null) {
                Handler handler = new Handler(Looper.getMainLooper());
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        logger.w(logLevel, BASE_TAG + tag, message);
                    }
                };
                handler.post(runnable);
            } else {
                Log.w(BASE_TAG + tag, message);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void e(final LogLevel logLevel, final String tag, final String message) {
        if (checkLogLevel(logLevel)) {
            if (interceptLogs && logger != null) {
                Handler handler = new Handler(Looper.getMainLooper());
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        logger.e(logLevel, BASE_TAG + tag, message);
                    }
                };
                handler.post(runnable);
            } else {
                Log.e(BASE_TAG + tag, message);
            }
        }
    }

    /**
     * Checks to see if log level of message confirms to set log level.
     *
     * @param logLevel {@link com.sailthru.android.sdk.impl.logger.Logger.LogLevel}
     * @return boolean
     */
    private boolean checkLogLevel(LogLevel logLevel) {
        if (logLevel.value <= this.logLevel.value) {
            return true;
        }

        return false;
    }
}