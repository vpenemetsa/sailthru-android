package com.sailthru.android.sdk;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Vijay Penemetsa on 5/19/14.
 */
class ASYNC_RegisterTask extends AsyncTask<Void, Void, String> {

    Context mContext;
    String mAppId;
    String mApiKey;
    String mUid;
    API_Constants.Identification mUserType;

    ASYNC_RegisterTask(Context context, String appId, String apiKey, String uid,
                       API_Constants.Identification userType) {
        mContext = context;
        mAppId = appId;
        mApiKey = apiKey;
        mUid = uid;
        mUserType = userType;
    }

    @Override
    protected String doInBackground(Void... params) {
        MODEL_UserRegisterAppResponse response = API_Manager.getInstance().registerUser(mContext,
                mAppId, mApiKey, mUid, mUserType);
        String text = response.getHid();

        return text;
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d("*********fskljdfhlskjdfg**********", s);
        Toast.makeText(mContext, s, Toast.LENGTH_LONG).show();
    }
}
