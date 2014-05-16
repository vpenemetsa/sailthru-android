package com.sailthru.android.sdk;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.sailthru.android.sdk.api.APIManager;
import com.sailthru.android.sdk.api.ApiConstants;
import com.sailthru.android.sdk.api.model.UserRegisterAppResponse;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Vijay Penemetsa on 5/14/14.
 */
public class SailthruClient extends AbstractSailthruClient {

    public SailthruClient(Context context, String apiKey, String apiSecret) {
        super(context, apiKey, apiSecret, ApiConstants.API_ENDPOINT);
    }

    public String getHorizonId() {
//        UserRegisterAppResponse response = APIManager.getInstance().registerUser();
        HorIdLoader loader = new HorIdLoader();
        String hid = null;
        try {
            hid = loader.execute((Void) null).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return hid;
    }

    public class HorIdLoader extends AsyncTask<Void, Void, String> {

        public HorIdLoader() {

        }

        @Override
        protected String doInBackground(Void... params) {
            UserRegisterAppResponse response = APIManager.getInstance().registerUser();
            String text = response.getHid();

            return text;
        }

        @Override
        protected void onPostExecute(String s) {
        }
    }

}
