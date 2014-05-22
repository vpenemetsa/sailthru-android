package com.sailthru.android.sdk;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import retrofit.Callback;


/**
 * Created by Vijay Penemetsa on 5/19/14.
 */
class ASYNC_RegisterTask extends AsyncTask<Void, Void, Void> {

    Context mContext;
    String mAppId;
    String mApiKey;
    String mUid;
    SailthruClient.Identification mUserType;
    ST_AuthenticatedClient mAuthenticatedClient;
    Callback<MODEL_UserRegisterAppResponse> mCallback;

    public ASYNC_RegisterTask(Context context, String appId, String apiKey, String uid,
                              SailthruClient.Identification userType,
                              ST_AuthenticatedClient authenticatedClient,
                              Callback<MODEL_UserRegisterAppResponse> callback) {
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

        if (UTILS_AppRegister.notNullOrEmpty(storedHid) &&
                !mUserType.equals(SailthruClient_Abstract.Identification.ANONYMOUS)) {
            Log.d("stored Hid", storedHid);
            API_Manager.getInstance(mAuthenticatedClient).registerUser(mContext,
                    mAppId, mApiKey, storedHid, null, mCallback);
        } else {
            API_Manager.getInstance(mAuthenticatedClient).registerUser(mContext,
                    mAppId, mApiKey, mUid, mUserType, mCallback);
        }

        return null;
    }
}