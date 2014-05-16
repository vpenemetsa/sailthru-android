package com.sailthru.android.sdk;

import android.content.Context;

/**
 * Created by Vijay Penemetsa on 5/14/14.
 */
public class AbstractSailthruClient {

    protected Context mContext;
    protected String mApiKey;
    protected String mApiSecret;
    protected String mApiEndpoint;

    public AbstractSailthruClient(Context context, String apiKey, String apiSecret, String apiEndpoint) {
        mContext = context;
        mApiKey = apiKey;
        mApiSecret = apiSecret;
        mApiEndpoint = apiEndpoint;
    }

}
