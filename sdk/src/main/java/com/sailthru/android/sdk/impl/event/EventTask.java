package com.sailthru.android.sdk.impl.event;

import com.sailthru.android.sdk.impl.async.AppTrackAsyncTask;
import com.sailthru.android.sdk.impl.logger.STLog;
import com.squareup.tape.Task;

/**
 * Created by Vijay Penemetsa on 5/28/14.
 *
 * A task which is enqueued in EventTaskQueue and executed by EventTaskService.
 */

public class EventTask implements Task<EventTask.EventCallback> {

    private static final String TAG = EventTask.class.getSimpleName();

    STLog log;

    public interface EventCallback {
        void onSuccess ();
        void onFailure();
        void onNotReachable();
    }

    public EventTask(Event event) {
        this.event = event;
        log = STLog.getInstance();
    }

    Event event;

    @Override
    public void execute(EventCallback callback) {
        try {
            event.setExecuteCount(event.getExecuteCount() + 1);
            AppTrackAsyncTask task = new AppTrackAsyncTask(event, callback);
            task.execute((Void) null);
            log.d(TAG, "Executing task ----------- 2");
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public Event getEvent() {
        return event;
    }
}