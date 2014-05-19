package com.sailthru.android.sdk;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Vijay Penemetsa on 5/14/14.
 */
public class SailthruClient {

    Context mContext;
    ASYNC_RegisterTask mHorIdLoader = null;

    public SailthruClient(Context context) {
        mContext = context;
    }

    public void register(API_Constants.RegistrationMode mode, String domain,
                         String apiKey, String appId, API_Constants.Identification identification,
                         String uid, String token) {

        if (mHorIdLoader != null) {
            mHorIdLoader.cancel(true);
        }

        ASYNC_RegisterTask loader = new ASYNC_RegisterTask(mContext, appId, apiKey, uid, identification);
        loader.execute((Void) null);
    }

}
