package com.sailthru.android.sdk.impl.event;

import android.util.Log;

import com.sailthru.android.sdk.impl.api.ApiManager;
import com.sailthru.android.sdk.impl.response.AppTrackResponse;
import com.squareup.tape.Task;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Vijay Penemetsa on 5/28/14.
 *
 * A task which is enqueued in EventTaskQueue and executed by EventTaskService.
 */
public class EventTask implements Task<EventTask.Callback> {

    private static final String TAG = EventTask.class.getSimpleName();

    public interface Callback {
        void onSuccess ();
        void onFailure();
    }

    public EventTask(Event event) {
        this.event = event;
    }

    Event event;
    int executeCount = 0;
    Callback callback;

    @Override
    public void execute(Callback callback) {
        try {
            executeCount++;
            this.callback = callback;
            ApiManager.sendEvent(event, appTrackCallback);
            Log.d(TAG, "Executing task ----------- 2");
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    private retrofit.Callback<AppTrackResponse> appTrackCallback =
            new retrofit.Callback<AppTrackResponse>() {
        @Override
        public void success(AppTrackResponse appTrackResponse, Response response) {
            if (appTrackResponse.getOk()) {
                callback.onSuccess();
            } else {
                callback.onFailure();
            }
        }

        @Override
        public void failure(RetrofitError error) {
            error.printStackTrace();
            callback.onFailure();
        }
    };

    public int getExecuteCount() {
        return executeCount;
    }
}