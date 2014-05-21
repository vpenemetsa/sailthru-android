package com.sailthru.android.sdk;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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

    Async_RegisterTask(Context context, String appId, String apiKey, String uid,
                       SailthruClient.Identification userType,
                       St_AuthenticatedClient authenticatedClient) {
        mContext = context;
        mAppId = appId;
        mApiKey = apiKey;
        mUid = uid;
        mUserType = userType;
        mAuthenticatedClient = authenticatedClient;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Api_Manager.getInstance(mAuthenticatedClient).registerUser(mContext,
                mAppId, mApiKey, mUid, mUserType);

        return null;
    }
}