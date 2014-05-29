package com.sailthru.android.sdk.impl.logger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Vijay Penemetsa on 5/22/14.
 */
public abstract class Logger {

    public static final String LOGGER_DATA_EXTRA = "LOGGER_DATA_EXTRA";

    public enum LogLevel {
        EVERYTHING, DEBUG, CRASHES
    }

    protected Context mContext;
    protected LogLevel mLogLevel;

    protected static boolean mInterceptLogs = false;

    protected abstract void onReceivedLog(String logMessage);

    public Logger(Context context, boolean interceptLogs) {
        mContext = context;
        mInterceptLogs = interceptLogs;
    }

    public void setLogLevel(LogLevel logLevel) {
        mLogLevel = logLevel;
    }

    private BroadcastReceiver mLogReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String logMessage = intent.getStringExtra(LOGGER_DATA_EXTRA);
            onReceivedLog(logMessage);
        }
    };

    public void sendLog(String logMessage) {
        if (mInterceptLogs) {
            onReceivedLog(logMessage);
        } else {
            Log.i("SailthruSDK", logMessage);
        }
    }
}