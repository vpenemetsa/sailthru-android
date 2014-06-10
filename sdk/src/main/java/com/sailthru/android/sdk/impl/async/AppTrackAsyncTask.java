package com.sailthru.android.sdk.impl.async;

import android.content.Context;
import android.os.AsyncTask;

import com.sailthru.android.sdk.impl.api.ApiManager;
import com.sailthru.android.sdk.impl.event.Event;
import com.sailthru.android.sdk.impl.event.EventTask;
import com.sailthru.android.sdk.impl.response.AppTrackResponse;

/**
 * Created by Vijay Penemetsa on 6/9/14.
 */
public class AppTrackAsyncTask extends AsyncTask<Void, Void, Boolean> {

    private static final String TAG = AppTrackAsyncTask.class.getSimpleName();

    private EventTask.EventCallback callback;
    private Event event;

    public AppTrackAsyncTask(Event event, EventTask.EventCallback callback) {
        this.event = event;
        this.callback = callback;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            ApiManager apiManager = new ApiManager();
            AppTrackResponse response = apiManager.sendEvent(event);
            if (response != null && response.getOk()) {
                    return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            cancel(true);
        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if (aBoolean) {
            callback.onSuccess();
        } else {
            callback.onFailure();
        }
    }
}