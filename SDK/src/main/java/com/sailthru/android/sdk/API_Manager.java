package com.sailthru.android.sdk;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpStatus;

import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Vijay Penemetsa on 5/14/14.
 *
 * A Central class to handle all API transactions
 */
class Api_Manager {

    private static final String TAG = Api_Manager.class.getSimpleName();
    private static St_AuthenticatedClient mAuthenticatedClient;

    private static final int REGISTER_USER = 1;

    public static Api_Manager getInstance(St_AuthenticatedClient client) {
        mAuthenticatedClient = client;
        return new Api_Manager();
    }

    public interface RegisterUserService {
        @FormUrlEncoded
        @POST("/userregisterapp")
        void registerUser(@Field(Api_Constants.UR_SIG_KEY) String sig,
                                             @Field(Api_Constants.UR_JSON_KEY) String json,
                                             @Field(Api_Constants.UR_API_KEY) String apiKey,
                                             @Field(Api_Constants.UR_FORMAT_KEY) String format,
                                             Callback<Model_UserRegisterAppResponse> callback);
    }

    public void registerUser(final Context context, String appId,
                                                String apiKey, String uid,
                                                SailthruClient.Identification userType) {

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Api_Constants.API_ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        RegisterUserService service = adapter.create(RegisterUserService.class);

        Map<String, String> params = Utils_AppRegister.buildRequest(context, appId, apiKey,
                uid, userType);

        Callback<Model_UserRegisterAppResponse> registerCallback = new Callback<Model_UserRegisterAppResponse>() {
            @Override
            public void success(Model_UserRegisterAppResponse registerAppResponse, Response response) {
                if (response.getStatus() == HttpStatus.SC_OK) {
                    mAuthenticatedClient.saveHid(registerAppResponse.getHid());
                    Toast.makeText(context, registerAppResponse.getHid(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        };

        service.registerUser(
            params.get(Api_Constants.UR_SIG_KEY),
            params.get(Api_Constants.UR_JSON_KEY),
            apiKey,
            Api_Constants.UR_FORMAT_VALUE, registerCallback);
    }

}