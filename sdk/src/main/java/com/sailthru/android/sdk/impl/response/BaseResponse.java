package com.sailthru.android.sdk.impl.response;

/**
 * Created by Vijay Penemetsa on 5/14/14.
 *
 * Base Response class extended by all other *Response classes to track
 * API response codes
 */
public class BaseResponse {

    private int statusCode;

    private String statusText;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }
}
