package com.sailthru.android.sdk.impl.api;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.sailthru.android.sdk.impl.event.Event;
import com.sailthru.android.sdk.impl.response.AppTrackResponse;
import com.sailthru.android.sdk.impl.response.UserRegisterAppResponse;
import com.sailthru.android.sdk.impl.utils.AppRegisterUtils;
import com.sailthru.android.sdk.Sailthru;
import com.sailthru.android.sdk.impl.utils.AppTrackUtils;
import com.sailthru.android.sdk.impl.utils.UtilsModule;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.ObjectGraph;
import retrofit.Callback;
import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.converter.GsonConverter;

/**
 * Created by Vijay Penemetsa on 5/14/14.
 *
 * A Central class to handle all API transactions
 */
public class ApiManager {

    private static final String TAG = ApiManager.class.getSimpleName();

    private static ApiManager apiManager;
    private static GsonConverter gsonConverter;

    public ApiManager(Context context) {
        ObjectGraph.create(new UtilsModule(context)).inject(this);
        gsonConverter = new GsonConverter(new Gson());
    }

    public static ApiManager getInstance(Context context) {
        if (apiManager == null) {
            apiManager = new ApiManager(context);
        }

        return apiManager;
    }

    @Inject
    static AppRegisterUtils appRegisterUtils;
    @Inject
    static AppTrackUtils appTrackUtils;

    public static void registerUser(Context context, String appId,
                                    String apiKey, String uid,
                                    Sailthru.Identification userType,
                                    Callback<UserRegisterAppResponse> callback) {

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ApiConstants.ST_API_ENDPOINT)
                .setConverter(gsonConverter)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        ApiInterfaces.RegisterUserService service = adapter.create(
                ApiInterfaces.RegisterUserService.class);

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

    public static AppTrackResponse sendEvent(Event event) {

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ApiConstants.HORIZON_API_ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(gsonConverter)
                .setErrorHandler(new AppTrackErrorHandler())
                .build();
        ApiInterfaces.AppTrackService service = adapter.create(ApiInterfaces.AppTrackService.class);

        Map<String, String> parameters = appTrackUtils.buildRequest(event);

        AppTrackResponse response = null;
        try {
            response = service.sendTags(parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    private static class AppTrackErrorHandler implements ErrorHandler {

        @Override
        public Throwable handleError(RetrofitError cause) {
            cause.printStackTrace();
            return null;
        }
    }
}