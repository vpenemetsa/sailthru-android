package com.sailthru.android.sdk.impl.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.sailthru.android.sdk.impl.api.ApiModule;
import com.sailthru.android.sdk.impl.client.AuthenticatedClient;
import com.sailthru.android.sdk.Sailthru;
import com.sailthru.android.sdk.impl.api.ApiManager;
import com.sailthru.android.sdk.impl.response.UserRegisterAppResponse;
import com.sailthru.android.sdk.impl.utils.AppRegisterUtils;
import com.sailthru.android.sdk.impl.utils.UtilsModule;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import dagger.ObjectGraph;
import retrofit.Callback;


/**
 * Created by Vijay Penemetsa on 5/19/14.
 *
 * Async Task to make App Registration request
 */
public class RegisterAsyncTask extends AsyncTask<Void, Void, Void> {

    @Inject
    AppRegisterUtils appRegisterUtils;

    Context context;
    String appId;
    String apiKey;
    String uid;
    Sailthru.Identification userType;
    AuthenticatedClient authenticatedClient;
    Callback<UserRegisterAppResponse> callback;

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

        ObjectGraph.create(new UtilsModule(context)).inject(this);
    }

    @Override
    protected Void doInBackground(Void... params) {
    String storedHid = authenticatedClient.getHid();

    if (appRegisterUtils.notNullOrEmpty(storedHid) &&
            !userType.equals(Sailthru.Identification.ANONYMOUS)) {
        Log.d("stored Hid", storedHid);
        ApiManager.registerUser(context, appId, apiKey, storedHid, null, callback);
    } else {
        ApiManager.registerUser(context, appId, apiKey, uid, userType, callback);
    }

    return null;
}
}