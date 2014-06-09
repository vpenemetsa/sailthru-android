package com.sailthru.android.sdk.impl.api;

import android.content.Context;
import android.util.Log;

import com.sailthru.android.sdk.impl.client.AuthenticatedClient;
import com.sailthru.android.sdk.impl.event.Event;
import com.sailthru.android.sdk.impl.response.AppTrackResponse;
import com.sailthru.android.sdk.impl.response.UserRegisterAppResponse;
import com.sailthru.android.sdk.impl.utils.AppRegisterUtils;
import com.sailthru.android.sdk.Sailthru;
import com.sailthru.android.sdk.impl.utils.AppTrackUtils;
import com.sailthru.android.sdk.impl.utils.UtilsModule;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.ObjectGraph;
import retrofit.Callback;
import retrofit.RestAdapter;

/**
 * Created by Vijay Penemetsa on 5/14/14.
 *
 * A Central class to handle all API transactions
 */
@Singleton
public class ApiManager {

    private static final String TAG = ApiManager.class.getSimpleName();
    private static AuthenticatedClient authenticatedClient;

    @Inject
    public ApiManager(Context context) {
        this.authenticatedClient = AuthenticatedClient.getInstance(context);
        ObjectGraph.create(UtilsModule.class).inject(this);
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

    public static void sendEvent(Event event, Callback<AppTrackResponse> callback) {

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ApiConstants.HORIZON_API_ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        ApiInterfaces.AppTrackService service = adapter.create(ApiInterfaces.AppTrackService.class);

        Map<String, String> parameters = AppTrackUtils.buildRequest(event);

        service.sendTags(parameters, callback);
    }
}