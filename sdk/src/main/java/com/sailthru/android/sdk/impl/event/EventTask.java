package com.sailthru.android.sdk.impl.event;

import com.sailthru.android.sdk.impl.async.AppTrackAsyncTask;
import com.sailthru.android.sdk.impl.logger.Logger;
import com.sailthru.android.sdk.impl.logger.STLog;
import com.squareup.tape.Task;

/**
 * Created by Vijay Penemetsa on 5/28/14.
 *
 * A task which is enqueued in EventTaskQueue and executed by
 * {@link com.sailthru.android.sdk.impl.event.SailthruAppTrackService}.
 */

public class EventTask implements Task<EventTask.EventCallback> {

    private static final String TAG = EventTask.class.getSimpleName();

    STLog log;

    public interface EventCallback {
        /**
         * Callback for a successful execution
         */
        void onSuccess ();

        /**
         * Callback for a unsuccessful execution
         */
        void onFailure();

        /**
         * Callback for a ENETUNREACH is encountered
         */
        void onNotReachable();
    }

    public EventTask(Event event) {
        this.event = event;
        log = STLog.getInstance();
    }

    Event event;

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(EventCallback callback) {
        try {
            event.setExecuteCount(event.getExecuteCount() + 1);
            AppTrackAsyncTask task = new AppTrackAsyncTask(event, callback);
            task.execute((Void) null);
            log.d(Logger.LogLevel.FULL, "EventTask", "Executing task");
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns {@link com.sailthru.android.sdk.impl.event.Event} object held by current task
     *
     * @return {@link com.sailthru.android.sdk.impl.event.Event}
     */
    public Event getEvent() {
        return event;
    }
}