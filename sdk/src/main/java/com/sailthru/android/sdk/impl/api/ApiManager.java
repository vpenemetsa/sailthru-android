package com.sailthru.android.sdk.impl.api;

import android.content.Context;

import com.google.inject.Inject;
import com.sailthru.android.sdk.impl.AuthenticatedClient;
import com.sailthru.android.sdk.Sailthru;
import com.sailthru.android.sdk.impl.response.UserRegisterAppResponse;
import com.sailthru.android.sdk.impl.utils.AppRegister;

import java.util.Map;

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
    private static AuthenticatedClient mAuthenticatedClient;

    @Inject
    public ApiManager(AuthenticatedClient client) {
        mAuthenticatedClient = client;
    }

//    public static API_Manager getInstance(AuthenticatedClient client) {
//        mAuthenticatedClient = client;
//        return new API_Manager();
//    }

    public interface RegisterUserService {
        @FormUrlEncoded
        @POST("/userregisterapp")
        void registerUser(@Field(Constants.UR_SIG_KEY) String sig,
                                                   @Field(Constants.UR_JSON_KEY) String json,
                                                   @Field(Constants.UR_API_KEY) String apiKey,
                                                   @Field(Constants.UR_FORMAT_KEY) String format,
                                                   Callback<UserRegisterAppResponse> callback);
    }

    public static void registerUser(Context context, String appId,
                                                      String apiKey, String uid,
                                                      Sailthru.Identification userType,
                                                      Callback<UserRegisterAppResponse> callback) {

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constants.API_ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        RegisterUserService service = adapter.create(RegisterUserService.class);

        Map<String, String> params = AppRegister.buildRequest(context, appId, apiKey,
                uid, userType);

        UserRegisterAppResponse response = new UserRegisterAppResponse();

        service.registerUser(
                params.get(Constants.UR_SIG_KEY),
                params.get(Constants.UR_JSON_KEY),
                apiKey,
                Constants.UR_FORMAT_VALUE,
                callback);
    }

    public static boolean sendEvents() {
        //TODO

        return true;
    }
}