package com.sailthru.android.sdk;

import android.content.Context;

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
class API_Manager {

    private static final String TAG = API_Manager.class.getSimpleName();

    public static API_Manager getInstance() {
        return new API_Manager();
    }

    public interface RegisterUserService {
        @FormUrlEncoded
        @POST("/userregisterapp")
        MODEL_UserRegisterAppResponse registerUser(@Field(API_Constants.UR_SIG_KEY) String sig,
                                             @Field(API_Constants.UR_JSON_KEY) String json,
                                             @Field(API_Constants.UR_API_KEY) String apiKey,
                                             @Field(API_Constants.UR_FORMAT_KEY) String format);
    }

    public MODEL_UserRegisterAppResponse registerUser(Context context, String appId,
                                                String apiKey, String uid,
                                                API_Constants.Identification userType) {

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(API_Constants.API_ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        RegisterUserService service = adapter.create(RegisterUserService.class);

        Map<String, String> params = UTILS_AppRegister.buildRequest(context, appId, apiKey,
                uid, userType);

        MODEL_UserRegisterAppResponse response = new MODEL_UserRegisterAppResponse();
        try {
            response = service.registerUser(
                    params.get(API_Constants.UR_SIG_KEY),
                    params.get(API_Constants.UR_JSON_KEY),
                    apiKey,
                    API_Constants.UR_FORMAT_VALUE);
            response.setStatusCode(200);
            response.setStatusText("success");
        } catch (RetrofitError error) {
            response.setStatusCode(error.getResponse().getStatus());
            response.setStatusText(error.getBody().toString());
        }

        return response;
/*
        /******APP KEY*******﹕ 419188ee0d0dc748f04e0cd9ea7d7c0f
        /******APP ID*******﹕ 5362a304fdd5ac3611000481
        /******JSON*******﹕ {"platform_app_id":"com.sailthru.qa","id":"dhoerl+testa009@sailthru.com","device_version":"4.3","device_type":"iphone","device_id":"32f1b4b34b5c0df0","env":"dev","platform_app_version":"1.0","key":"email","os_version":"7.1"}
        /******SIG*******﹕ 3fb2a09da32da85d4136492b361f3643
*/
//        return Observable.create(new Observable.OnSubscribe<MODEL_UserRegisterAppResponse>() {
//            @Override
//            public void call(Subscriber<? super MODEL_UserRegisterAppResponse> subscriber) {
//                try {
//                    subscriber.onNext(userService.registerUser(sig, json, apiKey, format));
//                    subscriber.onCompleted();
//                } catch (Exception e) {
//                    subscriber.onError(e);
//                }
//            }
//        }).subscribeOn(Schedulers.io());

//        return new MODEL_UserRegisterAppResponse();
//        public static Observable<MODEL_UserRegisterAppResponse> registerUser(final String city) {
//            return Observable.create(new Observable.OnSubscribe<MODEL_UserRegisterAppResponse>() {
//                @Override
//                public void call(Subscriber<? super MODEL_UserRegisterAppResponse> subscriber) {
//                    try {
//                        subscriber.onNext(apiManager.getWeather(city, "metric"));
//                        subscriber.onCompleted();
//                    } catch (Exception e) {
//                        subscriber.onError(e);
//                    }
//                }
//            }).subscribeOn(Schedule.io());
//        }
    }

}