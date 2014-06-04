package com.sailthru.android.sdk.impl.event;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.sailthru.android.sdk.impl.api.ApiManager;
import com.sailthru.android.sdk.impl.client.AuthenticatedClient;
import com.squareup.tape.Task;

import javax.inject.Inject;

/**
 * Created by Vijay Penemetsa on 5/28/14.
 *
 * A task which is enqueued in EventTaskQueue and executed by EventTaskService.
 */
public class EventTask implements Task<EventTask.Callback> {

    Event event;
    int executeCount = 0;

    private static final String TAG = EventTask.class.getSimpleName();

    public interface Callback {
        void onSuccess ();
        void onFailure();
    }

    public EventTask(Event event) {
        this.event = event;
    }

    @Override
    public void execute(final Callback callback) {
        try {
            executeCount++;

            //TODO: CREATE REQUEST

                boolean success = ApiManager.sendEvents();
                Log.d(TAG, "Executing task ----------- 2");
                if (success) {
                    callback.onSuccess();

                } else {
                    callback.onFailure();
                }

        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public int getExecuteCount() {
        return executeCount;
    }
}