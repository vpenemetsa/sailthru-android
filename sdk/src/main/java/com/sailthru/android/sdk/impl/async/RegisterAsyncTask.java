package com.sailthru.android.sdk.impl.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.sailthru.android.sdk.impl.AuthenticatedClient;
import com.sailthru.android.sdk.Sailthru;
import com.sailthru.android.sdk.impl.api.ApiManager;
import com.sailthru.android.sdk.impl.response.UserRegisterAppResponse;
import com.sailthru.android.sdk.impl.utils.AppRegister;

import retrofit.Callback;


/**
 * Created by Vijay Penemetsa on 5/19/14.
 *
 * Async Task to make App Registration request
 */
public class RegisterAsyncTask extends AsyncTask<Void, Void, Void> {

    Context mContext;
    String mAppId;
    String mApiKey;
    String mUid;
    Sailthru.Identification mUserType;
    AuthenticatedClient mAuthenticatedClient;
    Callback<UserRegisterAppResponse> mCallback;

    public RegisterAsyncTask(Context context, String appId, String apiKey, String uid,
                             Sailthru.Identification userType,
                             AuthenticatedClient authenticatedClient,
                             Callback<UserRegisterAppResponse> callback) {
        mContext = context;
        mAppId = appId;
        mApiKey = apiKey;
        mUid = uid;
        mUserType = userType;
        mAuthenticatedClient = authenticatedClient;
        mCallback = callback;
    }

    @Override
    protected Void doInBackground(Void... params) {
        String storedHid = mAuthenticatedClient.getHid();

        if (AppRegister.notNullOrEmpty(storedHid) &&
                !mUserType.equals(Sailthru.Identification.ANONYMOUS)) {
            Log.d("stored Hid", storedHid);
            ApiManager.registerUser(mContext, mAppId, mApiKey, storedHid, null, mCallback);
        } else {
            ApiManager.registerUser(mContext, mAppId, mApiKey, mUid, mUserType, mCallback);
        }

        return null;
    }
}