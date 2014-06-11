package com.sailthru.android.sdk.impl.api;

import android.content.Context;

import com.sailthru.android.sdk.impl.event.Event;
import com.sailthru.android.sdk.impl.logger.STLog;
import com.sailthru.android.sdk.impl.response.AppTrackResponse;
import com.sailthru.android.sdk.impl.response.UserRegisterAppResponse;
import com.sailthru.android.sdk.impl.utils.AppRegisterUtils;
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

    public void registerUser(Context context, String appId,
                                    String apiKey, String uid,
                                    Sailthru.Identification userType,
                                    Callback<UserRegisterAppResponse> callback) {

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ApiConstants.UR_API_ENDPOINT)
                .setLog(retrofitLogger)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        ApiInterfaces.RegisterUserService service = adapter.create(
                ApiInterfaces.RegisterUserService.class);

        AppRegisterUtils appRegisterUtils = new AppRegisterUtils();

        Map<String, String> params = appRegisterUtils.buildRequest(context, appId, apiKey,
                uid, userType);

        log.d("**********SIG****************", params.get(ApiConstants.UR_SIG_KEY));
        log.d("**********JSON***************", params.get(ApiConstants.UR_JSON_KEY));
        log.d("**********API KEY***********", apiKey);

        service.registerUser(
                params.get(ApiConstants.UR_SIG_KEY),
                params.get(ApiConstants.UR_JSON_KEY),
                apiKey,
                ApiConstants.UR_FORMAT_VALUE,
                callback);
    }

    public AppTrackResponse sendEvent(Event event) {
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ApiConstants.HORIZON_API_ENDPOINT + ApiConstants.API_HORIZON_PATH)
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
                log.d("**************************************", "" + response.getStatusCode());
            }
        }
        log.d("ApiManager", "7");
        return response;
    }

    public String getRecommendations(String domain, String hid, int count, List<String> tags) {
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ApiConstants.HORIZON_API_ENDPOINT)
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
                    log.e("RecommendParse", e.getMessage());
                }
            } catch (IOException e) {
                log.e("RecommendParse", e.getMessage());
            }

            response = sb.toString();
        } catch (RetrofitError e) {
            if (e.isNetworkError()) {
                log.d("Network", "No connection.");
            } else if (e.getResponse().getStatus() == HttpStatus.SC_NOT_FOUND) {
                log.d("Recommend", "No recommendations available");
            }
        }

        return response;
    }
}