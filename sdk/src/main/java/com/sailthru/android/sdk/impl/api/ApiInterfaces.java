package com.sailthru.android.sdk.impl.api;

import com.sailthru.android.sdk.impl.response.AppTrackResponse;
import com.sailthru.android.sdk.impl.response.UserRegisterAppResponse;

import java.util.Map;

import com.sailthru.android.sdk.impl.external.retrofit.Callback;
import com.sailthru.android.sdk.impl.external.retrofit.client.Response;
import com.sailthru.android.sdk.impl.external.retrofit.http.Field;
import com.sailthru.android.sdk.impl.external.retrofit.http.FormUrlEncoded;
import com.sailthru.android.sdk.impl.external.retrofit.http.GET;
import com.sailthru.android.sdk.impl.external.retrofit.http.POST;
import com.sailthru.android.sdk.impl.external.retrofit.http.QueryMap;


/**
 * Created by Vijay Penemetsa on 6/6/14.
 *
 * Contains interfaces for all api calls
 */
public class    ApiInterfaces {

    /**
     * Interface for UserRegister api call
     */
    public interface RegisterUserService {
        @FormUrlEncoded
        @POST(ApiConstants.API_USER_REGISTER_PATH)
        void registerUser(@Field(ApiConstants.UR_SIG_KEY) String sig,
                          @Field(ApiConstants.UR_JSON_KEY) String json,
                          @Field(ApiConstants.UR_API_KEY) String apiKey,
                          @Field(ApiConstants.UR_FORMAT_KEY) String format,
                          Callback<UserRegisterAppResponse> callback);
    }

    /**
     * Interface for AppTrack api call
     */
    public interface AppTrackService {
        @GET(ApiConstants.API_APPTRACK_PATH)
        AppTrackResponse sendTags(@QueryMap Map<String, String> parameters);
    }

    /**
     * Interface for Recommend endpoint
     */
    public interface RecommendService {
        @GET(ApiConstants.API_HORIZON_PATH + ApiConstants.API_RECOMMEND_PATH)
        Response getRecommendations(@QueryMap Map<String, String> parameters);
    }
}