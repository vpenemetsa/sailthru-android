package com.sailthru.android.sdk;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Vijay Penemetsa on 5/19/14.
 */
class Async_RegisterTask extends AsyncTask<Void, Void, String> {

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
    protected String doInBackground(Void... params) {
        Model_UserRegisterAppResponse response = Api_Manager.getInstance(mAuthenticatedClient).registerUser(mContext,
                mAppId, mApiKey, mUid, mUserType);
        String text = response.getHid();

        return text;
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d("*********Returned HID**********", s);
        Toast.makeText(mContext, s, Toast.LENGTH_LONG).show();
    }
}