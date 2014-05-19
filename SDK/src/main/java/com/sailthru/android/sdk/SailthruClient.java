package com.sailthru.android.sdk;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.sailthru.android.sdk.api.APIManager;
import com.sailthru.android.sdk.api.ApiConstants;
import com.sailthru.android.sdk.api.model.UserRegisterAppResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Vijay Penemetsa on 5/14/14.
 */
public class SailthruClient {

    Context mContext;
    HorIdLoader mHorIdLoader = null;

    public SailthruClient(Context context) {
        mContext = context;
    }

    public void register(ApiConstants.RegistrationMode mode, String domain,
                         String apiKey, String appId, ApiConstants.Identification identification,
                         String uid, String token) {

        if (mHorIdLoader != null) {
            mHorIdLoader.cancel(true);
        }

        HorIdLoader loader = new HorIdLoader(mContext, appId, apiKey, uid, identification);
        loader.execute((Void) null);
    }

//    private void getHorizonId() {
////        UserRegisterAppResponse response = APIManager.getInstance().registerUser();
//
//
//    }

    public class HorIdLoader extends AsyncTask<Void, Void, String> {

        Context mContext;
        String mAppId;
        String mApiKey;
        String mUid;
        ApiConstants.Identification mUserType;

        public HorIdLoader(Context context, String appId, String apiKey, String uid,
                           ApiConstants.Identification userType) {
            mContext = context;
            mAppId = appId;
            mApiKey = apiKey;
            mUid = uid;
            mUserType = userType;
        }

        @Override
        protected String doInBackground(Void... params) {
            UserRegisterAppResponse response = APIManager.getInstance().registerUser(mContext,
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

}
