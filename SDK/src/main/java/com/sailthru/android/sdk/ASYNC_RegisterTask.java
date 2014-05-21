package com.sailthru.android.sdk;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import retrofit.Callback;


/**
 * Created by Vijay Penemetsa on 5/19/14.
 */
class Async_RegisterTask extends AsyncTask<Void, Void, Void> {

    Context mContext;
    String mAppId;
    String mApiKey;
    String mUid;
    SailthruClient.Identification mUserType;
    St_AuthenticatedClient mAuthenticatedClient;
    Callback<Model_UserRegisterAppResponse> mCallback;

    public Async_RegisterTask(Context context, String appId, String apiKey, String uid,
                       SailthruClient.Identification userType,
                       St_AuthenticatedClient authenticatedClient,
                       Callback<Model_UserRegisterAppResponse> callback) {
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

        if (Utils_AppRegister.notNullOrEmpty(storedHid) &&
                !mUserType.equals(SailthruClient_Abstract.Identification.ANONYMOUS)) {
            Log.d("stored Hid", storedHid);
            Api_Manager.getInstance(mAuthenticatedClient).registerUser(mContext,
                    mAppId, mApiKey, storedHid, null, mCallback);
        } else {
            Api_Manager.getInstance(mAuthenticatedClient).registerUser(mContext,
                    mAppId, mApiKey, mUid, mUserType, mCallback);
        }

        return null;
    }
}