package com.sailthru.android.sdk.impl.event;

import android.content.Context;
import android.content.Intent;

import com.sailthru.android.sdk.impl.Constants;
import com.sailthru.android.sdk.impl.external.gson.src.main.java.com.st.gson.Gson;
import com.sailthru.android.sdk.impl.external.tape.FileObjectQueue;
import com.sailthru.android.sdk.impl.external.tape.ObjectQueue;
import com.sailthru.android.sdk.impl.external.tape.TaskQueue;
import com.sailthru.android.sdk.impl.logger.Logger;
import com.sailthru.android.sdk.impl.logger.STLog;

import java.io.File;
import java.io.IOException;

/**
 * Created by Vijay Penemetsa on 5/28/14.
 *
 * Queue managing events that were reported. The max queue size is 50
 * and the events are sent out in batches
 */
public class EventTaskQueue extends TaskQueue<EventTask> {

    private static final String TAG = EventTaskQueue.class.getSimpleName();
    private static final String FILENAME = "sailthru_event_task_queue";

    private final Context context;
    private static EventTaskQueue queue;

    STLog log;

    public EventTaskQueue(ObjectQueue<EventTask> delegate, Context context) {
        super(delegate);
        this.context = context;
        log = STLog.getInstance();
    }

    /**
     * Starts {@link SailthruAppTrackService}
     */
    private void startService() {
        context.startService(new Intent(context, SailthruAppTrackService.class));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(EventTask entry) {
        try {
            // Always maintain a queue of size less than MAX_QUEUE_SIZE
            if (size() == Constants.MAX_QUEUE_SIZE) {
                remove();
            }

            super.add(entry);
            log.d(Logger.LogLevel.BASIC, "EventTaskQueue", "Added Event task ---- " + size() + "");

            //Only sends tags if the size of queue exceeds QUEUE_SIZE_THRESHOLD
            if (size() >= Constants.QUEUE_SIZE_THRESHOLD) {
                startService();
            }
        } catch (Exception e) {
            log.d(Logger.LogLevel.FULL, "EventTaskQueue Add", e.toString());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove() {
        try {
            super.remove();
            log.d(Logger.LogLevel.BASIC, "EventTaskQueue", "Removed Event Task. Current size : "
                    + size());
        } catch (Exception e) {
            log.d(Logger.LogLevel.FULL, "EventTaskQueue Remove", e.toString());
        }
    }

    /**
     * Creates and initializes an {@link com.sailthru.android.sdk.impl.event.EventTaskQueue}
     *
     * @param context {@link android.content.Context}
     * @param gson {@link com.sailthru.android.sdk.impl.external.gson.src.main.java.com.st.gson.Gson}
     * @return {@link com.sailthru.android.sdk.impl.event.EventTaskQueue}
     */
    public static EventTaskQueue create(Context context, Gson gson) {
        if (queue == null) {
            FileObjectQueue<EventTask> delegate;
            try {
                FileObjectQueue.Converter<EventTask> converter =
                        new GsonConverter<EventTask>(gson, EventTask.class);
                File queueFile = new File(context.getFilesDir(), FILENAME);
                delegate = new FileObjectQueue<EventTask>(queueFile, converter);
            } catch (IOException e) {
                throw new RuntimeException("Unable to create queue", e);
            }

            queue = new EventTaskQueue(delegate, context);
        }

        return queue;
    }
}