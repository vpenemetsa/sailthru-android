package com.sailthru.android.sdk.impl.tests;

import android.test.InstrumentationTestCase;

import com.google.gson.Gson;
import com.sailthru.android.sdk.impl.client.AuthenticatedClient;
import com.sailthru.android.sdk.impl.event.Event;
import com.sailthru.android.sdk.impl.event.EventTask;
import com.sailthru.android.sdk.impl.event.EventTaskQueue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vijay Penemetsa on 5/31/14.
 */
public class EventTaskQueueTest extends InstrumentationTestCase {

    EventTaskQueue queue;

    AuthenticatedClient authenticatedClient;

    List<String> tags = new ArrayList<String>();
    Event event = new Event();
    EventTask eventTask;

    private static final long QUEUE_MAX_EXECUTION_TIME = 5000;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        queue = EventTaskQueue.create(getInstrumentation().getContext(), new Gson());
        for (int i = 0; i < 100; i++) {
            tags.add("kdljhflkjsghsldkjfhlskjdfgslkdjfglskjdfgslkjdfsdkljfg");
        }
        event.addTags(tags);
        String url = "www.google.com";
        event.setUrl(url);
        eventTask = new EventTask(event);
        authenticatedClient = AuthenticatedClient.getInstance(getInstrumentation().getContext());
    }

    public void testAddToQueue() throws Exception {
        int initialSize = queue.size();
        queue.add(eventTask);

        assertEquals(initialSize + 1, queue.size());
    }

    public void testRemoveFromQueue() throws Exception {
        int initialSize = queue.size();
        if (initialSize == 0) {
            queue.add(eventTask);
            initialSize = 1;
        }

        queue.remove();

        assertEquals(initialSize - 1, queue.size());
    }

    public void testQueueExecution() throws Exception {
        int initialSize = queue.size();
        for (int i = 0; i < (20 - initialSize); i++) {
            queue.add(eventTask);
        }

        Thread.sleep(QUEUE_MAX_EXECUTION_TIME);

        assertEquals(queue.size(), 0);
    }
}