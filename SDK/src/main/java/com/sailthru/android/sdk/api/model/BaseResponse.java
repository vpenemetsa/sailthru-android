package com.sailthru.android.sdk.api.model;

/**
 * Created by Vijay Penemetsa on 5/14/14.
 *
 * Base Response class extended by all other *Response classes to track
 * API response codes
 */
class BaseResponse {

    private String statusCode;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }
}
