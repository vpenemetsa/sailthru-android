package com.sailthru.android.sdk.impl.api;

import android.content.Context;
import android.os.Build;

import com.sailthru.android.sdk.impl.event.Event;
import com.sailthru.android.sdk.impl.logger.Logger;
import com.sailthru.android.sdk.impl.logger.STLog;
import com.sailthru.android.sdk.impl.response.AppTrackResponse;
import com.sailthru.android.sdk.impl.response.UserRegisterAppResponse;
import com.sailthru.android.sdk.impl.utils.UserRegisterUtils;
import com.sailthru.android.sdk.Sailthru;
import com.sailthru.android.sdk.impl.utils.AppTrackUtils;
import com.sailthru.android.sdk.impl.utils.RecommendUtils;

import org.apache.http.HttpStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by Vijay Penemetsa on 5/14/14.
 *
 * A Central class to handle all API transactions
 */
public class ApiManager {

    private static final String TAG = ApiManager.class.getSimpleName();

    Context context;
    STLog log;
    RetrofitLogger retrofitLogger;

    public ApiManager() {
        log = STLog.getInstance();
        retrofitLogger = RetrofitLogger.getInstance();
    }

    public ApiManager(Context context) {
        this.context = context;
        log = STLog.getInstance();
        retrofitLogger = RetrofitLogger.getInstance();
    }

    /**
     * Makes call to AppRegister endpoint to register a user/device
     *
     * @param context {@link android.content.Context}
     * @param appId String
     * @param apiKey String
     * @param uid String
     * @param userType {@link com.sailthru.android.sdk.Sailthru.Identification}
     * @param callback {@link retrofit.Callback}
     */
    public void registerUser(Context context, String env, String appId,
                                    String apiKey, String uid,
                                    Sailthru.Identification userType,
                                    String platformAppId,
                                    Callback<UserRegisterAppResponse> callback) {

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ApiConstants.UR_API_ENDPOINT)
                .setLog(retrofitLogger)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        ApiInterfaces.RegisterUserService service = adapter.create(
                ApiInterfaces.RegisterUserService.class);

        UserRegisterUtils userRegisterUtils = new UserRegisterUtils();

        Map<String, String> params = userRegisterUtils.buildRequest(context, env, appId, apiKey,
                uid, userType, platformAppId);

        service.registerUser(
                params.get(ApiConstants.UR_SIG_KEY),
                params.get(ApiConstants.UR_JSON_KEY),
                apiKey,
                ApiConstants.UR_FORMAT_VALUE,
                callback);
    }

    /**
     * Used to send individual events containing tags for AppTrack
     *
     * @param event {@link com.sailthru.android.sdk.impl.event.Event}
     * @return {@link com.sailthru.android.sdk.impl.response.AppTrackResponse}
     */
    public AppTrackResponse sendEvent(Event event) {
        String horizonEndpoint;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
            horizonEndpoint = ApiConstants.HORIZON_API_ENDPOINT;
        } else {
            horizonEndpoint = ApiConstants.HORIZON_API_ENDPOINT_GINGERBREAD;
        }

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(horizonEndpoint + ApiConstants.API_HORIZON_PATH)
                .setLog(retrofitLogger)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        ApiInterfaces.AppTrackService service = adapter.create(ApiInterfaces.AppTrackService.class);

        AppTrackUtils utils = new AppTrackUtils();
        Map<String, String> parameters = utils.buildRequest(event);

        AppTrackResponse response = null;
        try {
            response = service.sendTags(parameters);

        } catch (RetrofitError e) {
            if (e.isNetworkError()) {
                response = new AppTrackResponse();
                response.setStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                log.d(Logger.LogLevel.FULL, "Network Error", "" + response.getStatusCode());
            }
        }

        return response;
    }

    /**
     * Calls Recommend endpoint with optional filter parameters and
     * returns a JSON String of recommendations
     *
     * @param domain String
     * @param hid String
     * @param count int
     * @param tags List<String>
     * @return String
     */
    public String getRecommendations(String domain, String hid, int count, List<String> tags) {
        String horizonEndpoint;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
            horizonEndpoint = ApiConstants.HORIZON_API_ENDPOINT;
        } else {
            horizonEndpoint = ApiConstants.HORIZON_API_ENDPOINT_GINGERBREAD;
        }

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(horizonEndpoint)
                .setLog(retrofitLogger)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        ApiInterfaces.RecommendService service = adapter.create(
                ApiInterfaces.RecommendService.class);

        RecommendUtils recommendUtils = new RecommendUtils();
        Map<String, String> parameters = recommendUtils.buildRequest(domain, hid, count, tags);

        String response = "";
        try {
            Response recommendResponse = service.getRecommendations(parameters);
            BufferedReader reader = null;
            StringBuilder sb = new StringBuilder();
            try {
                reader = new BufferedReader(new InputStreamReader(
                        recommendResponse.getBody().in()));

                String line = null;
                try {
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                } catch (IOException e) {
                    log.e(Logger.LogLevel.FULL, "Error while reading recommend response",
                            e.getMessage());
                }
            } catch (IOException e) {
                log.e(Logger.LogLevel.FULL, "Error while reading recommend response", e.getMessage());
            }

            response = sb.toString();
        } catch (RetrofitError e) {
            if (e.isNetworkError()) {
                log.d(Logger.LogLevel.BASIC, "Network", "No connection.");
            } else if (e.getResponse().getStatus() == HttpStatus.SC_NOT_FOUND) {
                log.d(Logger.LogLevel.BASIC, "Recommend", "No recommendations available");
            }
        }

        return response;
    }
}