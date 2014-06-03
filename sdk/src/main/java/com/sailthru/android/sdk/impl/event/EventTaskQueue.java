package com.sailthru.android.sdk.impl.event;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
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

    private static final int MAX_QUEUE_SIZE = 50;
    private static final int QUEUE_SIZE_THRESHOLD = 20;

    private final Context context;

    private static EventTaskQueue queue;

    @Inject
    public EventTaskQueue(ObjectQueue<EventTask> delegate, Context context) {
        super(delegate);
        this.context = context;

        if (size() > 0) {
            startService();
        }
    }

    private void startService() {
        context.startService(new Intent(context, EventTaskService.class));
    }

    @Override
    public void add(EventTask entry) {
        // Always maintain a queue of size less than 50 elements
        if (size() == MAX_QUEUE_SIZE) {
            remove();
        }

        super.add(entry);
        Log.d(TAG, "Added Event task ---- " + size() + "");

        //Only sends tags if the size of queue exceeds 20 elements
        if (size() >= QUEUE_SIZE_THRESHOLD) {
            startService();
            Log.d(TAG, "Started service");
        }
    }

    @Override
    public void remove() {
        Log.d(TAG, "Removed Event Task ----- " + size() + "");
        super.remove();

    }

    public static EventTaskQueue create(Context context, Gson gson) {
        Log.d(TAG, "Creating task queue");
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

        return queue;
    }
}