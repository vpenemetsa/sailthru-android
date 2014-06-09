package com.sailthru.android.sdk.impl.event;

import android.util.Log;

import com.sailthru.android.sdk.impl.async.AppTrackAsyncTask;
import com.squareup.tape.Task;

/**
 * Created by Vijay Penemetsa on 5/28/14.
 *
 * A task which is enqueued in EventTaskQueue and executed by EventTaskService.
 */

public class EventTask implements Task<EventTask.EventCallback> {

    private static final String TAG = EventTask.class.getSimpleName();

    public interface EventCallback {
        void onSuccess ();
        void onFailure();
    }

    public EventTask(Event event) {
        this.event = event;
    }

    Event event;
    int executeCount = 0;
    EventCallback callback;

    @Override
    public void execute(EventCallback callback) {
        try {
            executeCount++;
            this.callback = callback;
            AppTrackAsyncTask task = new AppTrackAsyncTask(event, this.callback);
            task.execute((Void) null);
            Log.d(TAG, "Executing task ----------- 2");
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public int getExecuteCount() {
        return executeCount;
    }
}