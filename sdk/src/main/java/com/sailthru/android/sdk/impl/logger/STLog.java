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
     * @param logger {@link com.sailthru.android.sdk.impl.logger.Logger}
     */
    public void setExternalLogger(Logger logger) {
        this.logger = logger;
        interceptLogs = true;
    }

    public Logger getExternalLogger() {
        return logger;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void d(final LogLevel logLevel, final String tag, final String message) {
        Log.d("!!!!!!!!!!!!", "log d");
        if (interceptLogs && logger != null) {
            Log.d("!!!!!!!!!!!!", "log d : intercept and logger");
            if (checkLogLevel(logLevel)) {
                Log.d("!!!!!!!!!!!!", "log d : checked and good");
                Handler handler = new Handler(Looper.getMainLooper());
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        logger.d(logLevel, BASE_TAG + tag, message);
                    }
                };
                handler.post(runnable);
            }
        } else {
            Log.d(BASE_TAG + tag, message);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void w(final LogLevel logLevel, final String tag, final String message) {
        Log.d("!!!!!!!!!!!!", "log w");
        if (interceptLogs && logger != null) {
            Log.d("!!!!!!!!!!!!", "log w : intercept and logger");
            if (checkLogLevel(logLevel)) {
                Log.d("!!!!!!!!!!!!", "log w : checked and good");
                Handler handler = new Handler(Looper.getMainLooper());
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        logger.w(logLevel, BASE_TAG + tag, message);
                    }
                };
                handler.post(runnable);
            }
        } else {
            Log.w(BASE_TAG + tag, message);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void e(final LogLevel logLevel, final String tag, final String message) {
        Log.d("!!!!!!!!!!!!", "log e");
        if (interceptLogs && logger != null) {
            Log.d("!!!!!!!!!!!!", "log e : intercept and logger");
            if (checkLogLevel(logLevel)) {
                Log.d("!!!!!!!!!!!!", "log e : checked and good");
                Handler handler = new Handler(Looper.getMainLooper());
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        logger.e(logLevel, BASE_TAG + tag, message);
                    }
                };
                handler.post(runnable);
            }
        } else {
            Log.e(BASE_TAG + tag, message);
        }
    }

    /**
     * Checks to see if log level of message confirms to set log level.
     *
     * @param logLevel {@link com.sailthru.android.sdk.impl.logger.Logger.LogLevel}
     * @return boolean
     */
    private boolean checkLogLevel(LogLevel logLevel) {
        if (logger == null) {
            Log.d("!!!!!!!!!!!!", "checkLogLevel : false");
            return false;
        }

        if (logger.logLevel.equals(LogLevel.NONE)) {
            Log.d("!!!!!!!!!!!!", "checkLogLevel : none");
            return false;
        }

        if (logger.logLevel.equals(LogLevel.BASIC)) {
            Log.d("!!!!!!!!!!!!", "checkLogLevel : basic");
            if (logLevel.equals(LogLevel.BASIC)) {
                Log.d("!!!!!!!!!!!!", "checkLogLevel : basic : basic");
                return true;
            }
        }

        if (logger.logLevel.equals(LogLevel.FULL)) {
            Log.d("!!!!!!!!!!!!", "checkLogLevel : full");
            return true;
        }

        return false;
    }
}