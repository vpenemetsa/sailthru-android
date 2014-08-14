package com.sailthru.android.sdk.impl.async;

import android.content.Context;
import android.os.AsyncTask;

import com.sailthru.android.sdk.impl.client.AuthenticatedClient;
import com.sailthru.android.sdk.Sailthru;
import com.sailthru.android.sdk.impl.api.ApiManager;
import com.sailthru.android.sdk.impl.response.UserRegisterAppResponse;

import com.sailthru.android.sdk.impl.external.retrofit.Callback;

/**
 * Created by Vijay Penemetsa on 5/19/14.
 *
 * Async Task to make App Registration request
 */
public class UserRegisterAsyncTask extends AsyncTask<Void, Void, Void> {

    Context context;
    String env;
    String appId;
    String apiKey;
    String uid;
    Sailthru.Identification userType;
    String platformAppId;
    String gcmRegId;
    AuthenticatedClient authenticatedClient;
    Callback<UserRegisterAppResponse> callback;
    ApiManager apiManager;

    public UserRegisterAsyncTask(Context context, String env, String appId, String apiKey,
                                 String uid, Sailthru.Identification userType, String platformAppId,
                                 String gcmRegId, AuthenticatedClient authenticatedClient,
                                 Callback<UserRegisterAppResponse> callback) {
        this.context = context;
        this.env = env;
        this.appId = appId;
        this.apiKey = apiKey;
        this.uid = uid;
        this.userType = userType;
        this.platformAppId = platformAppId;
        this.gcmRegId = gcmRegId;
        this.authenticatedClient = authenticatedClient;
        this.callback = callback;
        apiManager = new ApiManager(context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Void doInBackground(Void... params) {
        apiManager.registerUser(context, env, appId, apiKey, uid, userType, platformAppId, callback);
        return null;
    }
}