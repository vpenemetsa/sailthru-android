package com.sailthru.android.sdk.impl.async;

import android.content.Context;
import android.os.AsyncTask;

import com.sailthru.android.sdk.impl.client.AuthenticatedClient;
import com.sailthru.android.sdk.Sailthru;
import com.sailthru.android.sdk.impl.api.ApiManager;
import com.sailthru.android.sdk.impl.response.UserRegisterAppResponse;
import com.sailthru.android.sdk.impl.utils.AppRegisterUtils;

import retrofit.Callback;

/**
 * Created by Vijay Penemetsa on 5/19/14.
 *
 * Async Task to make App Registration request
 */
public class RegisterAsyncTask extends AsyncTask<Void, Void, Void> {

    Context context;
    String appId;
    String apiKey;
    String uid;
    Sailthru.Identification userType;
    AuthenticatedClient authenticatedClient;
    Callback<UserRegisterAppResponse> callback;
    AppRegisterUtils appRegisterUtils;
    ApiManager apiManager;

    public RegisterAsyncTask(Context context, String appId, String apiKey, String uid,
                             Sailthru.Identification userType,
                             AuthenticatedClient authenticatedClient,
                             Callback<UserRegisterAppResponse> callback) {
        this.context = context;
        this.appId = appId;
        this.apiKey = apiKey;
        this.uid = uid;
        this.userType = userType;
        this.authenticatedClient = authenticatedClient;
        this.callback = callback;
        appRegisterUtils = new AppRegisterUtils();
        apiManager = new ApiManager(context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Void doInBackground(Void... params) {
        String storedHid = authenticatedClient.getHid();
        String id;

        if (userType.equals(Sailthru.Identification.ANONYMOUS)) {
            if (appRegisterUtils.notNullOrEmpty(storedHid)) {
                id = storedHid;
                userType = null;
            } else {
                id = uid;
            }
        } else {
            id = uid;
        }

        apiManager.registerUser(context, appId, apiKey, id, userType, callback);

        return null;
    }
}