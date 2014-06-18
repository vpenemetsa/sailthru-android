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

    /**
     * {@inheritDoc}
     */
    @Override
    public void d(final String tag, final String message) {
        if (interceptLogs && logger != null) {
            Handler handler = new Handler(Looper.getMainLooper());
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    logger.d(BASE_TAG + tag, message);
                }
            };
            handler.post(runnable);
        } else {
            Log.d(BASE_TAG + tag, message);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void w(final String tag, final String message) {
        if (interceptLogs && logger != null) {
            Handler handler = new Handler(Looper.getMainLooper());
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    logger.w(BASE_TAG + tag, message);
                }
            };
            handler.post(runnable);
        } else {
            Log.w(BASE_TAG + tag, message);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void e(final String tag, final String message) {
        if (interceptLogs && logger != null) {
            Handler handler = new Handler(Looper.getMainLooper());
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    logger.e(BASE_TAG + tag, message);
                }
            };
            handler.post(runnable);
        } else {
            Log.e(BASE_TAG + tag, message);
        }
    }
}