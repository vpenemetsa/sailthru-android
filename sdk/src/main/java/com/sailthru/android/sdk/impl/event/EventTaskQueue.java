package com.sailthru.android.sdk.impl.event;

import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.sailthru.android.sdk.impl.Constants;
import com.sailthru.android.sdk.impl.logger.STLog;
import com.squareup.tape.FileObjectQueue;
import com.squareup.tape.ObjectQueue;
import com.squareup.tape.TaskQueue;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Vijay Penemetsa on 5/28/14.
 *
 * Queue managing events that were reported. The max queue size is 50
 * and the events are sent out in batches
 */
@Singleton
public class EventTaskQueue extends TaskQueue<EventTask> {

    private static final String TAG = EventTaskQueue.class.getSimpleName();
    private static final String FILENAME = "sailthru_event_task_queue";

    private final Context context;
    private static EventTaskQueue queue;

    STLog log;

    @Inject
    public EventTaskQueue(ObjectQueue<EventTask> delegate, Context context) {
        super(delegate);
        this.context = context;
        log = STLog.getInstance();
    }

    /**
     * Starts {@link com.sailthru.android.sdk.impl.event.EventTaskService}
     */
    private void startService() {
        context.startService(new Intent(context, EventTaskService.class));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(EventTask entry) {
        try {
            // Always maintain a queue of size less than 50 elements
            if (size() == Constants.MAX_QUEUE_SIZE) {
                remove();
            }

            super.add(entry);
            log.d(TAG, "Added Event task ---- " + size() + "");

            //Only sends tags if the size of queue exceeds 20 elements
            if (size() >= Constants.QUEUE_SIZE_THRESHOLD) {
                startService();
                log.d(TAG, "Started service");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove() {
        log.d(TAG, "Removed Event Task ----- " + size() + "");
        try {
            super.remove();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates and initializes an {@link com.sailthru.android.sdk.impl.event.EventTaskQueue}
     *
     * @param context {@link android.content.Context}
     * @param gson {@link com.google.gson.Gson}
     * @return {@link com.sailthru.android.sdk.impl.event.EventTaskQueue}
     */
    public static EventTaskQueue create(Context context, Gson gson) {
        try {
            if (queue == null) {
                FileObjectQueue.Converter<EventTask> converter = new GsonConverter<EventTask>(gson, EventTask.class);
                File queueFile = new File(context.getFilesDir(), FILENAME);
                FileObjectQueue<EventTask> delegate;
                try {
                    delegate = new FileObjectQueue<EventTask>(queueFile, converter);
                } catch (IOException e) {
                    throw new RuntimeException("Unable to create queue", e);
                }

                queue = new EventTaskQueue(delegate, context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return queue;
    }
}