package com.sailthru.android.sdk.impl.api;

import android.content.Context;
import android.util.Log;

import com.sailthru.android.sdk.impl.client.AuthenticatedClient;
import com.sailthru.android.sdk.Sailthru;
import com.sailthru.android.sdk.impl.response.UserRegisterAppResponse;
import com.sailthru.android.sdk.impl.utils.AppRegisterUtils;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Vijay Penemetsa on 5/14/14.
 *
 * A Central class to handle all API transactions
 */
public class ApiManager {

    private static final String TAG = ApiManager.class.getSimpleName();
    private static AuthenticatedClient authenticatedClient;

    public ApiManager(AuthenticatedClient authenticatedClient) {
        this.authenticatedClient = authenticatedClient;
    }

    @Inject
    static AppRegisterUtils appRegisterUtils;

//    public static API_Manager getInstance(AuthenticatedClient client) {
//        mAuthenticatedClient = client;
//        return new API_Manager();
//    }

    public interface RegisterUserService {
        @FormUrlEncoded
        @POST("/userregisterapp")
        void registerUser(@Field(ApiConstants.UR_SIG_KEY) String sig,
                                                   @Field(ApiConstants.UR_JSON_KEY) String json,
                                                   @Field(ApiConstants.UR_API_KEY) String apiKey,
                                                   @Field(ApiConstants.UR_FORMAT_KEY) String format,
                                                   Callback<UserRegisterAppResponse> callback);
    }

    public static void registerUser(Context context, String appId,
                                                      String apiKey, String uid,
                                                      Sailthru.Identification userType,
                                                      Callback<UserRegisterAppResponse> callback) {

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ApiConstants.API_ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        RegisterUserService service = adapter.create(RegisterUserService.class);

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

    public static boolean sendEvents() {
        //TODO

        return true;
    }
}