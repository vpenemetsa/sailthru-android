package com.sailthru.android.sdk.impl.async;

import android.os.AsyncTask;

import com.sailthru.android.sdk.impl.api.ApiManager;
import com.sailthru.android.sdk.impl.event.Event;
import com.sailthru.android.sdk.impl.event.EventTask;
import com.sailthru.android.sdk.impl.response.AppTrackResponse;

import org.apache.http.HttpStatus;

/**
 * Created by Vijay Penemetsa on 6/9/14.
 *
 * Async task to send AppTrack tags
 */
public class AppTrackAsyncTask extends AsyncTask<Void, Void, AppTrackResponse> {

    private static final String TAG = AppTrackAsyncTask.class.getSimpleName();

    private EventTask.EventCallback callback;
    private Event event;

    public AppTrackAsyncTask(Event event, EventTask.EventCallback callback) {
        this.event = event;
        this.callback = callback;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected AppTrackResponse doInBackground(Void... params) {
        AppTrackResponse response = new AppTrackResponse();
        try {
            ApiManager apiManager = new ApiManager();
            response = apiManager.sendEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
            cancel(true);
        }

        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPostExecute(AppTrackResponse response) {
        if (response == null) {
            callback.onFailure();
        } else if (response.getOk()){
            callback.onSuccess();
        } else if (response.getStatusCode() == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
            callback.onNotReachable();
        }
    }
}