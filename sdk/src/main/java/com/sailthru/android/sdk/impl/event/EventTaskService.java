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
import android.util.Log;

import com.sailthru.android.sdk.impl.Constants;

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

    private EventTask currentTask;

    private static final String TAG = EventTaskService.class.getSimpleName();
    private static final long EVENT_TASK_MAX_EXECUTIONS = 3;
    private boolean isConnectedToNetwork;

    BroadcastReceiver networkStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            isConnectedToNetwork = intent.getBooleanExtra(
                    Constants.INTENT_EXTRA_NETWORK_STATUS, false);
            Log.d(TAG + " BR Received", isConnectedToNetwork + "");

            if (isConnectedToNetwork && queue != null) {
                if (queue.size() > Constants.QUEUE_SIZE_THRESHOLD) {
                    execute();
                }
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        ObjectGraph.create(new EventModule(getApplicationContext())).inject(this);
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
        Log.d("SERVICCCCEEEEEE", "STart command received");
        execute();
        return START_STICKY;
    }

    private void execute() {
        Log.d("***********Service Execute*************", isConnectedToNetwork + "");
        try {
            if (queue.size() > 0) {
                if (isConnectedToNetwork) {
                    currentTask = queue.peek();
                    Log.d(TAG, "Executing service");
                    if (currentTask != null) {
                        Log.d(TAG, "Executing task");
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
        Log.d(TAG, "Removing task from queue");
        try {
            queue.remove();
            Log.d(TAG, "Size after remove ----- " + queue.size());
            if (queue.size() > 0) {
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
            queue.remove();
            if (currentTask.getEvent().getExecuteCount() < EVENT_TASK_MAX_EXECUTIONS) {
                queue.add(currentTask);
            }
            execute();
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