package com.sailthru.android.sdk.event;

import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.squareup.otto.Bus;
import com.squareup.otto.Produce;
import com.squareup.tape.FileObjectQueue;
import com.squareup.tape.ObjectQueue;
import com.squareup.tape.TaskQueue;

import java.io.File;
import java.io.IOException;


/**
 * Created by Vijay Penemetsa on 5/28/14.
 */
public class EventTaskQueue extends TaskQueue<EventTask> {

    private static final String FILENAME = "event_task_queue";

    private final Context mContext;
    private final Bus mBus;

    public EventTaskQueue(ObjectQueue<EventTask> delegate, Context context, Bus bus) {
        super(delegate);
        mContext = context;
        mBus = bus;
        bus.register(this);

        if (size() > 0) {
            startService();
        }
    }

    private void startService() {
        mContext.startService(new Intent(mContext, EventTaskService.class));
    }

    @Override
    public void add(EventTask entry) {
        // Always maintain a queue of size less than 50 elements
        if (size() == 50) {
            remove();
        }

        super.add(entry);
        mBus.post(produceSizeChanged());

        //Only sends tags if the size of queue exceeds 20 elements
        if (size() >= 20) {
            startService();
        }
    }

    @Override
    public void remove() {
        super.remove();
        mBus.post(produceSizeChanged());
    }

    @Produce
    public EventQueueSize produceSizeChanged() {
        return new EventQueueSize(size());
    }

    public static EventTaskQueue create(Context context, Gson gson, Bus bus) {
        FileObjectQueue.Converter<EventTask> converter = new GsonConverter<EventTask>(gson, EventTask.class);
        File queueFile = new File(context.getFilesDir(), FILENAME);
        FileObjectQueue<EventTask> delegate;
        try {
            delegate = new FileObjectQueue<EventTask>(queueFile, converter);
        } catch (IOException e) {
            throw new RuntimeException("Unable to create queue", e);
        }

        return new EventTaskQueue(delegate, context, bus);
    }
}