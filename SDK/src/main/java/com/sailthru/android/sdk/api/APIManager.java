package com.sailthru.android.sdk.api;

import com.google.gson.GsonBuilder;
import com.sailthru.android.sdk.api.model.UserRegisterAppResponse;

//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.http.converter.FormHttpMessageConverter;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.http.converter.json.GsonHttpMessageConverter;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import retrofit.RestAdapter;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

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