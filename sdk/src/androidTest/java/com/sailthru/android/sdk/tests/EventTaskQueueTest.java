package com.sailthru.android.sdk.tests;

import android.test.InstrumentationTestCase;

import com.sailthru.android.sdk.impl.external.gson.Gson;
import com.sailthru.android.sdk.impl.utils.SailthruUtils;
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
    SailthruUtils sailthruUtils;

    private static final long QUEUE_MAX_EXECUTION_TIME = 5000;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        Gson gson = new Gson();
        queue = EventTaskQueue.create(getInstrumentation().getContext(), gson);
        for (int i = 0; i < 5; i++) {
            tags.add("kdljhflkjsghsldkjfhlskjdfgslkdjfglskjdfgslkjdfsdkljfg");
        }
        event.addTags(tags);
        String url = "www.sailthru.com";
        event.setUrl(url);
        eventTask = new EventTask(event);
        authenticatedClient = AuthenticatedClient.getInstance(getInstrumentation().getContext());
        sailthruUtils = new SailthruUtils(getInstrumentation().getContext(), authenticatedClient);
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
        for (int i = initialSize; i <= 3; i++) {
            queue.add(eventTask);
        }

        Thread.sleep(QUEUE_MAX_EXECUTION_TIME);

        assertEquals(queue.size(), 0);
    }

    public void testQueueInputValidation() throws Exception {
        List<String> tags = new ArrayList<String>();
        tags.add("green");
        String url = "www.google.com";

        boolean checkAppTrackInput = sailthruUtils.checkAppTrackData(tags, url);
        assertTrue(checkAppTrackInput);

        checkAppTrackInput = sailthruUtils.checkAppTrackData(null, url);
        assertTrue(checkAppTrackInput);

        checkAppTrackInput = sailthruUtils.checkAppTrackData(tags, null);
        assertTrue(checkAppTrackInput);

        checkAppTrackInput = sailthruUtils.checkAppTrackData(null, null);
        assertFalse(checkAppTrackInput);

        tags.clear();
        checkAppTrackInput = sailthruUtils.checkAppTrackData(tags, null);
        assertFalse(checkAppTrackInput);
    }
}