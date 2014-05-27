package com.sailthru.android.sdk.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.sailthru.android.sdk.ST_AuthenticatedClient;
import com.sailthru.android.sdk.SailthruClient_Abstract;
import com.sailthru.android.sdk.api.Manager;
import com.sailthru.android.sdk.response.UserRegisterAppResponse;
import com.sailthru.android.sdk.utils.AppRegister;

import retrofit.Callback;


/**
 * Created by Vijay Penemetsa on 5/19/14.
 */
public class RegisterTask extends AsyncTask<Void, Void, Void> {

    Context mContext;
    String mAppId;
    String mApiKey;
    String mUid;
    SailthruClient_Abstract.Identification mUserType;
    ST_AuthenticatedClient mAuthenticatedClient;
    Callback<UserRegisterAppResponse> mCallback;

    public RegisterTask(Context context, String appId, String apiKey, String uid,
                        SailthruClient_Abstract.Identification userType,
                        ST_AuthenticatedClient authenticatedClient,
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

        Manager apiManager = new Manager(mAuthenticatedClient);

        if (AppRegister.notNullOrEmpty(storedHid) &&
                !mUserType.equals(SailthruClient_Abstract.Identification.ANONYMOUS)) {
            Log.d("stored Hid", storedHid);
            apiManager.registerUser(mContext, mAppId, mApiKey, storedHid, null, mCallback);
        } else {
            apiManager.registerUser(mContext, mAppId, mApiKey, mUid, mUserType, mCallback);
        }

        return null;
    }
}