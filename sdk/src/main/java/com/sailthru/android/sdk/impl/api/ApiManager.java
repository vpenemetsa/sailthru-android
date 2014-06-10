package com.sailthru.android.sdk.impl.api;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.common.api.Api;
import com.sailthru.android.sdk.impl.event.Event;
import com.sailthru.android.sdk.impl.response.AppTrackResponse;
import com.sailthru.android.sdk.impl.response.UserRegisterAppResponse;
import com.sailthru.android.sdk.impl.utils.AppRegisterUtils;
import com.sailthru.android.sdk.Sailthru;
import com.sailthru.android.sdk.impl.utils.AppTrackUtils;

import java.util.Map;

import retrofit.Callback;
import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

/**
 * Created by Vijay Penemetsa on 5/14/14.
 *
 * A Central class to handle all API transactions
 */
public class ApiManager {

    private static final String TAG = ApiManager.class.getSimpleName();

    Context context;

    public ApiManager() {

    }

    public ApiManager(Context context) {
        this.context = context;
    }

    public void registerUser(Context context, String appId,
                                    String apiKey, String uid,
                                    Sailthru.Identification userType,
                                    Callback<UserRegisterAppResponse> callback) {

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ApiConstants.ST_API_ENDPOINT)

                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        ApiInterfaces.RegisterUserService service = adapter.create(
                ApiInterfaces.RegisterUserService.class);

        AppRegisterUtils appRegisterUtils = new AppRegisterUtils();

        Map<String, String> params = appRegisterUtils.buildRequest(context, appId, apiKey,
                uid, userType);

        Log.d("**********SIG****************", params.get(ApiConstants.UR_SIG_KEY));
        Log.d("**********JSON***************", params.get(ApiConstants.UR_JSON_KEY));
        Log.d("**********API KEY***********", apiKey);

        service.registerUser(
                params.get(ApiConstants.UR_SIG_KEY),
                params.get(ApiConstants.UR_JSON_KEY),
                apiKey,
                ApiConstants.UR_FORMAT_VALUE,
                callback);
    }

    public AppTrackResponse sendEvent(Event event) {
        Log.d("ApiManager", "1");
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ApiConstants.HORIZON_API_ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setErrorHandler(new AppTrackErrorHandler())
                .build();
        Log.d("ApiManager", "2");
        ApiInterfaces.AppTrackService service = adapter.create(ApiInterfaces.AppTrackService.class);
        Log.d("ApiManager", "3");

        AppTrackUtils utils = new AppTrackUtils();
        Map<String, String> parameters = utils.buildRequest(event);

        Log.d("ApiManager", "4");
        AppTrackResponse response = null;
        try {
            Log.d("ApiManager", "5");
            response = service.sendTags(parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("ApiManager", "7");
        return response;
    }

    private static class AppTrackErrorHandler implements ErrorHandler {

        @Override
        public Throwable handleError(RetrofitError cause) {
            cause.printStackTrace();
            Log.d("ApiManager", "8");
            return cause;
        }
    }
}