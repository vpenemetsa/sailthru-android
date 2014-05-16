package com.sailthru.android.sdk.api;

import com.sailthru.android.sdk.api.model.UserRegisterAppResponse;

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

    public UserRegisterAppResponse registerUser() {
//        String url = ApiConstants.API_ENDPOINT + ApiConstants.USER_REGISTER_ENDPOINT;

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ApiConstants.API_ENDPOINT)
                .build();

        RegisterUserService service = adapter.create(RegisterUserService.class);

        return service.registerUser("d48ffaf86db3afaa1d7201ca8ac25bac",
                ApiConstants.TEMP_JSON,
                "419188ee0d0dc748f04e0cd9ea7d7c0f",
                "json");

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