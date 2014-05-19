package com.sailthru.android.sdk.api;

import android.content.Context;
import android.util.Log;

import com.sailthru.android.sdk.api.model.UserRegisterAppResponse;
import com.sailthru.android.sdk.utils.AppRegisterUtils;

import java.util.Map;

import retrofit.RestAdapter;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import rx.Observable;
import rx.Subscriber;
import rx.android.observables.AndroidObservable;
import rx.schedulers.Schedulers;

/**
 * Created by Vijay Penemetsa on 5/14/14.
 *
 * A Central class to handle all API transactions
 */
public class APIManager {

    private static final String TAG = APIManager.class.getSimpleName();

    public static APIManager getInstance() {
        return new APIManager();
    }

    public interface RegisterUserService {
        @FormUrlEncoded
        @POST("/userregisterapp")
        UserRegisterAppResponse registerUser(@Field("sig") String sig,
                                             @Field("json") String json,
                                             @Field("api_key") String apiKey,
                                             @Field("format") String format);
    }

    public UserRegisterAppResponse registerUser(Context context, String appId,
                                                String apiKey, String uid,
                                                ApiConstants.Identification userType) {

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ApiConstants.API_ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        RegisterUserService service = adapter.create(RegisterUserService.class);

        Map<String, String> params = AppRegisterUtils.buildRequest(context, appId, apiKey,
                uid, userType);

        return service.registerUser(params.get(ApiConstants.UR_SIG_KEY),
                params.get(ApiConstants.UR_JSON_KEY), apiKey, ApiConstants.UR_FORMAT_VALUE);
/*
        /******APP KEY*******﹕ 419188ee0d0dc748f04e0cd9ea7d7c0f
        /******APP ID*******﹕ 5362a304fdd5ac3611000481
        /******JSON*******﹕ {"platform_app_id":"com.sailthru.qa","id":"dhoerl+testa009@sailthru.com","device_version":"4.3","device_type":"iphone","device_id":"32f1b4b34b5c0df0","env":"dev","platform_app_version":"1.0","key":"email","os_version":"7.1"}
        /******SIG*******﹕ 3fb2a09da32da85d4136492b361f3643
*/
//        return Observable.create(new Observable.OnSubscribe<UserRegisterAppResponse>() {
//            @Override
//            public void call(Subscriber<? super UserRegisterAppResponse> subscriber) {
//                try {
//                    subscriber.onNext(userService.registerUser(sig, json, apiKey, format));
//                    subscriber.onCompleted();
//                } catch (Exception e) {
//                    subscriber.onError(e);
//                }
//            }
//        }).subscribeOn(Schedulers.io());

//        return new UserRegisterAppResponse();
//        public static Observable<UserRegisterAppResponse> registerUser(final String city) {
//            return Observable.create(new Observable.OnSubscribe<UserRegisterAppResponse>() {
//                @Override
//                public void call(Subscriber<? super UserRegisterAppResponse> subscriber) {
//                    try {
//                        subscriber.onNext(apiManager.getWeather(city, "metric"));
//                        subscriber.onCompleted();
//                    } catch (Exception e) {
//                        subscriber.onError(e);
//                    }
//                }
//            }).subscribeOn(Schedule.io());
//        }
//        MultiValueMap<String, String> postParams = new LinkedMultiValueMap<String, String>();
//        postParams.add("sig", "d48ffaf86db3afaa1d7201ca8ac25bac");
//        postParams.add("json", ApiConstants.TEMP_JSON);
//        postParams.add("api_key", "419188ee0d0dc748f04e0cd9ea7d7c0f");
//        postParams.add("format", "json");
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
//        messageConverters.add(new FormHttpMessageConverter());
//
//        HttpEntity<?> requestEntity = new HttpEntity<Object>(postParams, headers);
//
//        RestTemplate template = new RestTemplate();
//        GsonBuilder builder = new GsonBuilder();
//        template.getMessageConverters().add(new GsonHttpMessageConverter(builder.create()));
//        ResponseEntity<UserRegisterAppResponse> responseEntity = template.exchange(url, HttpMethod.POST, requestEntity, UserRegisterAppResponse.class);

//        return responseEntity.getBody();
    }
}