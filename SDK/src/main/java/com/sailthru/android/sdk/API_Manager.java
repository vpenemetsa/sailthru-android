package com.sailthru.android.sdk;

import android.content.Context;
import android.util.Log;

import java.util.Map;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
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

    public static Api_Manager getInstance(St_AuthenticatedClient client) {
        mAuthenticatedClient = client;
        return new Api_Manager();
    }

    public interface RegisterUserService {
        @FormUrlEncoded
        @POST("/userregisterapp")
        Model_UserRegisterAppResponse registerUser(@Field(Api_Constants.UR_SIG_KEY) String sig,
                                             @Field(Api_Constants.UR_JSON_KEY) String json,
                                             @Field(Api_Constants.UR_API_KEY) String apiKey,
                                             @Field(Api_Constants.UR_FORMAT_KEY) String format);
    }

    public Model_UserRegisterAppResponse registerUser(Context context, String appId,
                                                String apiKey, String uid,
                                                SailthruClient.Identification userType) {

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Api_Constants.API_ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

//        Log.d("!!!!!!!!!!!APi manager!!!!!!!!!!", Looper.getMainLooper().getThread().toString());
//        Log.d("!!!!!!!!!!!APi manager!!!!!!!!!!", Thread.currentThread().toString());
        RegisterUserService service = adapter.create(RegisterUserService.class);

        Map<String, String> params = Utils_AppRegister.buildRequest(context, appId, apiKey,
                uid, userType);

        Model_UserRegisterAppResponse response = new Model_UserRegisterAppResponse();
        try {
            response = service.registerUser(
                    params.get(Api_Constants.UR_SIG_KEY),
                    params.get(Api_Constants.UR_JSON_KEY),
                    apiKey,
                    Api_Constants.UR_FORMAT_VALUE);
            response.setStatusCode(200);
            response.setStatusText("success");
            mAuthenticatedClient.saveHid(response.getHid());
//            Utils_SecurePreferences prefs = new Utils_SecurePreferences(context,
//                    St_Constants.ST_SECURE_PREFS, St_Constants.ST_SECURE_PREFS_KEY, true);
//            prefs.put(St_Constants.ST_PREFS_CACHED_HID, response.getHid());
        } catch (RetrofitError error) {
            response.setStatusCode(error.getResponse().getStatus());
            response.setStatusText(error.getBody().toString());
        }

        return response;
    }
}