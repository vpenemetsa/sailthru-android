package com.sailthru.android.sdk.impl.event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vijay Penemetsa on 5/28/14.
 *
 * Model for sending an Event request
 */
class Event {

    private List<String> tags = new ArrayList<String>();

    private String url;

    private long timestamp;

    private int retryCount;

    public List<String> getTags() {
        return tags;
    }

    public void addTags(List<String> tags) {
        this.tags.addAll(tags);
    }

    public void addTag(String tag) {
        tags.add(tag);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }
}
