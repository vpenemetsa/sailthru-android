package com.sailthru.android.sdk.impl.event;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.sailthru.android.sdk.impl.Constants;
import com.sailthru.android.sdk.impl.logger.Logger;
import com.sailthru.android.sdk.impl.logger.STLog;

/**
 * Created by Vijay Penemetsa on 5/28/14.
 *
 * Background service to execute EventTasks in EventTaskQueue sequentially
 */
public class SailthruAppTrackService extends Service implements EventTask.EventCallback {

    EventTaskQueue queue;

    STLog log;
    private EventTask currentTask;

    private static final String TAG = SailthruAppTrackService.class.getSimpleName();
    private static final long EVENT_TASK_MAX_EXECUTIONS = 3;
    private boolean isConnectedToNetwork;

    //Broadcast Receiver listens for network status broadcasts
    BroadcastReceiver networkStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            isConnectedToNetwork = intent.getBooleanExtra(
                    Constants.INTENT_EXTRA_NETWORK_STATUS, false);
            log.d(Logger.LogLevel.FULL, TAG + " Broadcast Received", isConnectedToNetwork + "");

            if (isConnectedToNetwork && queue != null) {
                if (queue.size() >= Constants.QUEUE_SIZE_THRESHOLD) {
                    execute();
                }
            }
        }
    };

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate() {
        super.onCreate();
        log = STLog.getInstance();
        queue = EventTaskQueue.create(getApplicationContext(), new Gson());
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo == null) {
            isConnectedToNetwork = false;
        } else {
            isConnectedToNetwork = true;
        }

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(
                networkStatusReceiver, new IntentFilter(Constants.ST_BROADCAST_NETWORK_STATUS));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        log.d(Logger.LogLevel.FULL, "AppTrackService", "Start command received");
        execute();
        return START_STICKY;
    }

    /**
     * Executes the next task in {@link com.sailthru.android.sdk.impl.event.EventTaskQueue}
     */
    private void execute() {
        try {
            if (queue.size() > 0) {
                if (isConnectedToNetwork) {
                    currentTask = queue.peek();
                    log.d(Logger.LogLevel.FULL, "AppTrackService", "Executing service");
                    if (currentTask != null) {
                        log.d(Logger.LogLevel.FULL, "AppTrackService", "Executing task");
                        currentTask.execute(this);
                    } else {
                        queue.remove();
                        execute();
                    }
                }
            } else {
                stopSelf();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSuccess() {
        try {
            if (queue.size() > 0) {
                log.d(Logger.LogLevel.FULL, "AppTrackService", "Size after remove ----- " + queue.size());
                queue.remove();
                execute();
            } else {
                stopSelf();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onFailure() {
        try {
            if (queue.size() > 0) {
                queue.remove();

                if (currentTask.getEvent().getExecuteCount() < EVENT_TASK_MAX_EXECUTIONS) {
                    queue.add(currentTask);
                }

                if (isConnectedToNetwork) {
                    execute();
                }
            } else {
                stopSelf();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onNotReachable() {
        try {
            isConnectedToNetwork = false;
            onFailure();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getApplicationContext()).
                unregisterReceiver(networkStatusReceiver);
    }
}