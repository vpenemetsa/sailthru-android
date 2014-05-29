package com.sailthru.android.sdk.impl.event;

import android.os.Handler;
import android.os.Looper;

import com.sailthru.android.sdk.impl.api.ApiManager;
import com.squareup.tape.Task;

/**
 * Created by Vijay Penemetsa on 5/28/14.
 *
 * A task which is enqueued in EventTaskQueue and executed by EventTaskService.
 */
public class EventTask implements Task<EventTask.Callback> {

    Event mEvent;

    private static final Handler mMainThread = new Handler(Looper.getMainLooper());

    public interface Callback {
        void onSuccess ();
        void onFailure();
    }

    public EventTask(Event event) {
        mEvent = event;
    }

    @Override
    public void execute(final Callback callback) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //TODO: MAKE REQUEST

                    boolean success = ApiManager.sendEvents();

                    if (success) {
                        mMainThread.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onSuccess();
                            }
                        });
                    } else {
                        mMainThread.post(new Runnable() {
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