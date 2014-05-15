package com.sailthru.android.sdk;

import android.content.Context;

import com.sailthru.android.sdk.api.APIManager;
import com.sailthru.android.sdk.api.ApiConstants;
import com.sailthru.android.sdk.api.model.UserRegisterAppResponse;

/**
 * Created by Vijay Penemetsa on 5/14/14.
 */
public class SailthruClient extends AbstractSailthruClient {

    public SailthruClient(Context context, String apiKey, String apiSecret) {
        super(context, apiKey, apiSecret, ApiConstants.API_ENDPOINT);
    }

    public String getHorizonId() {
        UserRegisterAppResponse response = APIManager.getInstance().registerUser();
        return response.getHid();
    }
}
