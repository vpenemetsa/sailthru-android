package com.sailthru.android.sdk;

import android.content.Context;

import com.sailthru.android.sdk.impl.async.UserRegisterAsyncTask;
import com.sailthru.android.sdk.impl.client.AuthenticatedClient;
import com.sailthru.android.sdk.impl.event.Event;
import com.sailthru.android.sdk.impl.event.EventTask;
import com.sailthru.android.sdk.impl.event.EventTaskQueue;
import com.sailthru.android.sdk.impl.logger.Logger;
import com.sailthru.android.sdk.impl.logger.STLog;
import com.sailthru.android.sdk.impl.response.UserRegisterAppResponse;

import org.apache.http.HttpStatus;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Helper methods used by the main Sailthru class
 *
 * Created by Vijay Penemetsa on 5/20/14.
 */
class SailthruClient {

    private static final String TAG = SailthruClient.class.getSimpleName();

    Context context;
    UserRegisterAsyncTask userRegisterAsyncTask = null;
    AuthenticatedClient authenticatedClient;
    STLog log;

    public SailthruClient(Context context, AuthenticatedClient authenticatedClient) {
        this.authenticatedClient = authenticatedClient;
        this.context = context;
        log = STLog.getInstance();
    }

    /**
     * Saves credentials from client to Shared Preferences
     *
     * @param mode String
     * @param domain String
     * @param apiKey String
     * @param appId String
     * @param identification String
     * @param uid String
     * @param token String
     */
    protected void saveCredentials(String mode, String domain, String apiKey, String appId,
                                          String identification, String uid, String token) {
        authenticatedClient.setMode(mode);
        authenticatedClient.setDomain(domain);
        authenticatedClient.setApiKey(apiKey);
        authenticatedClient.setAppId(appId);
        authenticatedClient.setIdentification(identification);
        authenticatedClient.setUid(uid);
        authenticatedClient.setToken(token);
    }

    /**
     * Checks to see if input for registration conforms with standards
     *
     * @param mode {@link com.sailthru.android.sdk.Sailthru.RegistrationMode}
     * @param domain String
     * @param apiKey String
     * @param appId String
     * @param identification {@link com.sailthru.android.sdk.Sailthru.Identification}
     * @param uid String
     * @param token String
     * @return boolean
     */
    protected boolean passedSanityChecks(Sailthru.RegistrationMode mode, String domain,
                                         String apiKey, String appId,
                                         Sailthru.Identification identification, String uid,
                                         String token) {

        boolean passedChecks = true;

        if (mode == null) {
            passedChecks = false;
            log.e(Logger.LogLevel.BASIC, TAG, "Mode cannot be set to null");
        }
        if (domain == null) {
            passedChecks = false;
            log.e(Logger.LogLevel.BASIC, TAG, "Domain cannot be null");
        }
        if (apiKey == null) {
            passedChecks = false;
            log.e(Logger.LogLevel.BASIC, TAG, "API Key cannot be null");
        }
        if (appId == null) {
            passedChecks = false;
            log.e(Logger.LogLevel.BASIC, TAG, "APP ID cannot be null");
        }
        if (identification == null) {
            log.e(Logger.LogLevel.BASIC, TAG, "Identification cannot be null");
            passedChecks = false;
        }

        if (identification == Sailthru.Identification.EMAIL && uid == null) {
            passedChecks = false;
            log.e(Logger.LogLevel.BASIC, TAG, "UID cannot be null when Identification is set to " +
                    "EMAIL");
        }

        return passedChecks;
    }

    /**
     * Checks for valid Apptrack input
     *
     * @param tags List<String>
     * @param url String
     * @return boolean
     */
    protected boolean checkAppTrackData(List<String> tags, String url) {
        return !((tags == null || tags.size() == 0) && (url == null || url.isEmpty()));
    }

    /**
     * Makes a call to the ApiManager with input credentials based on UserType
     *
     * @param appId String
     * @param apiKey String
     * @param uid String
     * @param userType {@link com.sailthru.android.sdk.Sailthru.Identification}
     */
    protected void makeRegistrationRequest(String appId, String apiKey, String uid,
                                                  Sailthru.Identification userType) {

        // Cancel any running AppRegister calls
        if (userRegisterAsyncTask != null) {
            userRegisterAsyncTask.cancel(true);
        }

        userRegisterAsyncTask = new UserRegisterAsyncTask(context, appId, apiKey, uid, userType,
                authenticatedClient, mRegisterCallback);
        userRegisterAsyncTask.execute((Void) null);
    }

    /**
     * Builds an Event with given parameters and adds it to the TaskQueue
     *
     * @param tags List<String>
     * @param url String
     * @param latitude String
     * @param longitude String
     * @param eventTaskQueue {@link com.sailthru.android.sdk.impl.event.EventTaskQueue}
     */
    protected void addEventToQueue(List<String> tags, String url, String latitude,
                                          String longitude, EventTaskQueue eventTaskQueue) {
        //Checking to make sure hid, appId and domain are not null.
        if (authenticatedClient.getHid() == null || authenticatedClient.getAppId() == null ||
                authenticatedClient.getDomain() == null) {
            log.d(Logger.LogLevel.BASIC, TAG, "One of the Authentication parameters is null");
            return;
        }

        //Checking to make sure both tags and url are not part of the request
        if ((tags == null || tags.size() == 0) && url == null) {
            log.d(Logger.LogLevel.BASIC, TAG, "Invalid input. No tags or url");
            return;
        }

        StringBuilder tagsBuilder = new StringBuilder(0);
        if (tags != null && tags.size() > 0) {
            for (String tag : tags) {
                tagsBuilder.append(tag + ", ");
            }
        }
        log.d(Logger.LogLevel.BASIC, "", "Tags : " + tagsBuilder.toString());
        log.d(Logger.LogLevel.BASIC, "", "Url : " + url);

        Event event = new Event();
        event.addTags(tags);
        event.setUrl(url);
        event.setLatitude(latitude);
        event.setLongitude(longitude);
        event.setTimestamp(System.currentTimeMillis());
        event.setHid(authenticatedClient.getHid());
        event.setAppId(authenticatedClient.getAppId());
        event.setDomain(authenticatedClient.getDomain());
        EventTask eventTask = new EventTask(event);
        eventTaskQueue.add(eventTask);
    }

    /**
     * Callback for Registration request
     */
    protected Callback<UserRegisterAppResponse> mRegisterCallback =
            new Callback<UserRegisterAppResponse>() {
                @Override
                public void success(UserRegisterAppResponse registerAppResponse,
                                    Response response) {
                    if (response.getStatus() == HttpStatus.SC_OK) {
                        authenticatedClient.saveHid(registerAppResponse.getHid());
                        log.d(Logger.LogLevel.BASIC, "Authentication Succesful", "hid - "
                                + registerAppResponse.getHid());
                    } else {
                        log.d(Logger.LogLevel.BASIC, "Authentication Failure",
                                response.getReason());
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    log.d(Logger.LogLevel.BASIC, "Authentication Failure",
                            error.toString());
                }
            };

    /**
     * Checks to see if all parameters required for getting recommendations exist.
     *
     * @return boolean
     */
    protected boolean canGetRecommendations() {
        String hid = authenticatedClient.getHid();
        String domain = authenticatedClient.getDomain();
        if (hid == null || hid.isEmpty()) {
            log.e(Logger.LogLevel.BASIC, TAG, "Not registered. Try registering to get " +
                    "recommendations");
            return false;
        } else if (domain == null || domain.isEmpty()) {
            log.e(Logger.LogLevel.BASIC, TAG, "No domain registered.");
            return false;
        }

        return true;
    }
}