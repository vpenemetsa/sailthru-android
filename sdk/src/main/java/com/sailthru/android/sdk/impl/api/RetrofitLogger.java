package com.sailthru.android.sdk.impl.api;

import com.sailthru.android.sdk.impl.logger.Logger;
import com.sailthru.android.sdk.impl.logger.STLog;

import retrofit.RestAdapter;

/**
 * Created by Vijay Penemetsa on 6/11/14.
 *
 * Custom logger to capture network logs
 */
public class RetrofitLogger implements RestAdapter.Log {

    private static final String TAG = "Network";

    static STLog logger;
    static RetrofitLogger retrofitLogger;

    public RetrofitLogger() {
        this.logger = STLog.getInstance();
    }

    public static RetrofitLogger getInstance() {
        if (retrofitLogger == null) {
            retrofitLogger = new RetrofitLogger();
        }

        return retrofitLogger;
    }

    public void setLogLevel(RestAdapter.LogLevel level) {
        retrofitLogger.setLogLevel(level);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void log(String message) {
        logger.d(Logger.LogLevel.FULL, TAG, message);
    }
}