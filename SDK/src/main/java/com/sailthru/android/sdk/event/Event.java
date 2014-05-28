package com.sailthru.android.sdk.event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vijay Penemetsa on 5/28/14.
 */
public class Event {

    private List<String> mTags = new ArrayList<String>();

    private String mUrl;

    private long mTimestamp;

    private int mRetryCount;

    public List<String> getTags() {
        return mTags;
    }

    public void addTags(List<String> tags) {
        mTags.addAll(tags);
    }

    public void addTag(String tag) {
        mTags.add(tag);
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public long getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(long timestamp) {
        mTimestamp = timestamp;
    }

    public int getRetryCount() {
        return mRetryCount;
    }

    public void setRetryCount(int retryCount) {
        mRetryCount = retryCount;
    }
}
