package com.sailthru.android.sdk;

import android.content.Context;

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
class API_Manager {

    private static final String TAG = API_Manager.class.getSimpleName();
    private static ST_AuthenticatedClient mAuthenticatedClient;

    public static API_Manager getInstance(ST_AuthenticatedClient client) {
        mAuthenticatedClient = client;
        return new API_Manager();
    }

    public interface RegisterUserService {
        @FormUrlEncoded
        @POST("/userregisterapp")
        void registerUser(@Field(API_Constants.UR_SIG_KEY) String sig,
                                                   @Field(API_Constants.UR_JSON_KEY) String json,
                                                   @Field(API_Constants.UR_API_KEY) String apiKey,
                                                   @Field(API_Constants.UR_FORMAT_KEY) String format,
                                                   Callback<MODEL_UserRegisterAppResponse> callback);
    }

    public void registerUser(Context context, String appId,
                                                      String apiKey, String uid,
                                                      SailthruClient_Abstract.Identification userType,
                                                      Callback<MODEL_UserRegisterAppResponse> callback) {

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(API_Constants.API_ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        RegisterUserService service = adapter.create(RegisterUserService.class);

        Map<String, String> params = UTILS_AppRegister.buildRequest(context, appId, apiKey,
                uid, userType);

        MODEL_UserRegisterAppResponse response = new MODEL_UserRegisterAppResponse();

        service.registerUser(
                params.get(API_Constants.UR_SIG_KEY),
                params.get(API_Constants.UR_JSON_KEY),
                apiKey,
                API_Constants.UR_FORMAT_VALUE,
                callback);
    }
}