package com.sailthru.android.sdk.impl.event;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.sailthru.android.sdk.impl.api.ApiManager;
import com.squareup.tape.Task;

/**
 * Created by Vijay Penemetsa on 5/28/14.
 *
 * A task which is enqueued in EventTaskQueue and executed by EventTaskService.
 */
public class EventTask implements Task<EventTask.Callback> {

    Event event;

    private static final String TAG = EventTask.class.getSimpleName();

    private static final Handler mainThread = new Handler(Looper.getMainLooper());

    public interface Callback {
        void onSuccess ();
        void onFailure();
    }

    public EventTask(Event event) {
        this.event = event;
    }

    @Override
    public void execute(final Callback callback) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //TODO: MAKE REQUEST

                    boolean success = ApiManager.sendEvents();
                    Log.d(TAG, "Executing task ----------- 2");
                    if (success) {
                        mainThread.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onSuccess();
                            }
                        });
                    } else {
                        mainThread.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onFailure();
                            }
                        });
                    }

                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}