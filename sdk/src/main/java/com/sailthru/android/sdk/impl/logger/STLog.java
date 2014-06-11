package com.sailthru.android.sdk.impl.logger;

import android.util.Log;

/**
 * Created by Vijay Penemetsa on 6/11/14.
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

    public void setExternalLogger(Logger logger) {
        this.logger = logger;
        interceptLogs = true;
    }

    @Override
    public void d(String tag, String message) {
        if (interceptLogs && logger != null) {
            logger.d(BASE_TAG + tag, message);
        } else {
            Log.d(BASE_TAG + tag, message);
        }
    }

    @Override
    public void w(String tag, String message) {
        if (interceptLogs && logger != null) {
            logger.w(BASE_TAG + tag, message);
        } else {
            Log.w(BASE_TAG + tag, message);
        }
    }

    @Override
    public void e(String tag, String message) {
        if (interceptLogs && logger != null) {
            logger.e(BASE_TAG + tag, message);
        } else {
            Log.e(BASE_TAG + tag, message);
        }
    }
}