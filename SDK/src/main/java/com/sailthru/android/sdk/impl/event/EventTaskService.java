package com.sailthru.android.sdk.impl.event;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import javax.inject.Inject;

/**
 * Created by Vijay Penemetsa on 5/28/14.
 *
 * Background service to execute EventTasks in EventTaskQueue sequentially
 */
public class EventTaskService extends Service implements EventTask.Callback {

    @Inject EventTaskQueue mQueue;

    private boolean mRunning;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        execute();
        return START_STICKY;
    }

    private void execute() {
        if (mRunning) {
            return;
        }

        EventTask task = mQueue.peek();

        if (task != null) {
            mRunning = true;
            task.execute(this);
        } else {
            stopSelf();
        }
    }

    @Override
    public void onSuccess() {
        mRunning = false;
        mQueue.remove();
        execute();
    }

    @Override
    public void onFailure() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}