package com.sailthru.android.sdk.impl.event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vijay Penemetsa on 5/28/14.
 *
 * Model for sending an Event request
 */
public class Event {

    private List<String> tags;

    private String url;

    private long timestamp;

    private double latitude;

    private double longitude;

    private int retryCount;

    private String hid;

    private String appId;

    private String domain;

    public List<String> getTags() {
        return tags;
    }

    public void addTags(List<String> tags) {
        this.tags = tags;
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public String getHid() {
        return hid;
    }

    public void setHid(String hid) {
        this.hid = hid;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
