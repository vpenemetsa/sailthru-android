package com.sailthru.android.sdk.async;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by Vijay Penemetsa on 5/27/14.
 */
public class EventService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public EventService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //TODO
    }

}
