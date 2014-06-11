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

import com.sailthru.android.sdk.impl.Constants;
import com.sailthru.android.sdk.impl.logger.STLog;

import javax.inject.Inject;

import dagger.ObjectGraph;

/**
 * Created by Vijay Penemetsa on 5/28/14.
 *
 * Background service to execute EventTasks in EventTaskQueue sequentially
 */
public class EventTaskService extends Service implements EventTask.EventCallback {

    @Inject
    EventTaskQueue queue;

    STLog log;
    private EventTask currentTask;

    private static final String TAG = EventTaskService.class.getSimpleName();
    private static final long EVENT_TASK_MAX_EXECUTIONS = 3;
    private boolean isConnectedToNetwork;

    BroadcastReceiver networkStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            isConnectedToNetwork = intent.getBooleanExtra(
                    Constants.INTENT_EXTRA_NETWORK_STATUS, false);
            log.d(TAG + " BR Received", isConnectedToNetwork + "");

            if (isConnectedToNetwork && queue != null) {
                if (queue.size() >= Constants.QUEUE_SIZE_THRESHOLD) {
                    execute();
                }
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        ObjectGraph.create(new EventModule(getApplicationContext())).inject(this);
        log = STLog.getInstance();
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo == null) {
            isConnectedToNetwork = false;
        } else {
            isConnectedToNetwork = true;
        }

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(
                networkStatusReceiver, new IntentFilter(Constants.BROADCAST_NETWORK_STATUS));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        log.d("SERVICCCCEEEEEE", "STart command received");
        execute();
        return START_STICKY;
    }

    private void execute() {
        log.d("***********Service Execute*************", isConnectedToNetwork + "");
        try {
            if (queue.size() > 0) {
                if (isConnectedToNetwork) {
                    currentTask = queue.peek();
                    log.d(TAG, "Executing service");
                    if (currentTask != null) {
                        log.d(TAG, "Executing task");
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

    @Override
    public void onSuccess() {
        try {
            if (queue.size() > 0) {
                log.d(TAG, "Size after remove ----- " + queue.size());
                queue.remove();
                execute();
            } else {
                stopSelf();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    @Override
    public void onNotReachable() {
        try {
            isConnectedToNetwork = false;
            onFailure();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getApplicationContext()).
                unregisterReceiver(networkStatusReceiver);
    }
}