package com.sailthru.android.sdk.impl.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.sailthru.android.sdk.Sailthru;
import com.sailthru.android.sdk.impl.async.GCMRegistrationAsyncTask;
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

import com.sailthru.android.sdk.impl.external.retrofit.Callback;
import com.sailthru.android.sdk.impl.external.retrofit.RetrofitError;
import com.sailthru.android.sdk.impl.external.retrofit.client.Response;


/**
 * Helper methods used by the main Sailthru class
 *
 * Created by Vijay Penemetsa on 5/20/14.
 */
public class SailthruUtils {

    private static final String TAG = SailthruUtils.class.getSimpleName();

    private Context context;
    private UserRegisterAsyncTask userRegisterAsyncTask = null;
    private GCMRegistrationAsyncTask gcmRegisterAsyncTask = null;
    private AuthenticatedClient authenticatedClient;
    private STLog log;

    public SailthruUtils(Context context, AuthenticatedClient authenticatedClient) {
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
     */
    public void saveCredentials(String mode, String domain, String apiKey, String appId,
                                          String identification, String uid, String platformAppId) {
        authenticatedClient.setMode(mode);
        authenticatedClient.setDomain(domain);
        authenticatedClient.setApiKey(apiKey);
        authenticatedClient.setAppId(appId);
        authenticatedClient.setIdentification(identification);
        authenticatedClient.setUid(uid);
        authenticatedClient.setPlatformAppId(platformAppId);
    }

    /**
     * Checks to see if input for registration conforms with standards
     *
     * @param mode {@link com.sailthru.android.sdk.Sailthru.RegistrationEnvironment}
     * @param domain String
     * @param apiKey String
     * @param appId String
     * @param identification {@link com.sailthru.android.sdk.Sailthru.Identification}
     * @param uid String
     * @return boolean
     */
    public boolean passedSanityChecks(Sailthru.RegistrationEnvironment mode, String domain,
                                         String apiKey, String appId,
                                         Sailthru.Identification identification, String uid,
                                         String platformAppId, String projectNumber) {

        boolean passedChecks = true;

        if (mode == null) {
            passedChecks = false;
            log.e(Logger.LogLevel.BASIC, TAG, "Environment cannot be set to null");
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

        if (platformAppId == null || platformAppId.isEmpty()) {
            passedChecks = false;
            log.e(Logger.LogLevel.BASIC, TAG, "Invalid platform app id.");
        }

        if (projectNumber == null || projectNumber.isEmpty()) {
            passedChecks = false;
            log.e(Logger.LogLevel.BASIC, TAG, "Invalid GCM Project Number");
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
    public boolean checkAppTrackData(List<String> tags, String url) {
        return !((tags == null || tags.size() == 0) && (url == null || url.isEmpty()));
    }

    /**
     * Makes a call to the ApiManager with input credentials based on UserType
     *
     * @param env String
     * @param appId String
     * @param apiKey String
     * @param uid String
     * @param userType {@link com.sailthru.android.sdk.Sailthru.Identification}
     * @param platformAppId String
     * @param projectNumber String
     */
    public void makeRegistrationRequest(String env, String appId, String apiKey, String uid,
                                                  Sailthru.Identification userType,
                                                  String platformAppId, String projectNumber) {
        if (checkPlayServices()) {
            String regId = getRegistrationId(context);
            if (regId.isEmpty()) {
                if (gcmRegisterAsyncTask != null) {
                    gcmRegisterAsyncTask.cancel(true);
                }
                gcmRegisterAsyncTask = new GCMRegistrationAsyncTask(context, env, appId, apiKey,
                        uid, userType, platformAppId, projectNumber, authenticatedClient,
                        mRegisterCallback, log);
                gcmRegisterAsyncTask.execute((Void) null);
            } else {
                if (userRegisterAsyncTask != null) {
                    userRegisterAsyncTask.cancel(true);
                }
                userRegisterAsyncTask = new UserRegisterAsyncTask(context, env, appId, apiKey, uid,
                        userType, platformAppId, regId, authenticatedClient, mRegisterCallback);
                userRegisterAsyncTask.execute((Void) null);
            }
        } else {
            log.e(Logger.LogLevel.BASIC, TAG, "Play services not available. Import play services " +
                    "to register");
        }
    }

    /**
     * Checks to see if play services are available on current device.
     *
     * @return Boolean
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            log.d(Logger.LogLevel.BASIC, TAG, "Play services not available.");
            return false;
        }

        return true;
    }

    /**
     * Checks and returns GCM Registration ID.
     *
     * @param context {@link android.content.Context}
     * @return String
     */
    private String getRegistrationId(Context context) {
        String registrationId = authenticatedClient.getGcmRegId();
        if (registrationId == null || registrationId.isEmpty()) {
            return "";
        }

        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        String registeredVersion = authenticatedClient.getAppVersion();
        int currentVersion = getAppVersion(context);
        if (Integer.parseInt(registeredVersion) != currentVersion) {
            return "";
        }
        return registrationId;
    }

    /**
     * Returns current version of app
     *
     * @param context {@link android.content.Context}
     * @return int
     */
    private int getAppVersion(Context context) {
        int versionCode = 0;
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            log.d(Logger.LogLevel.BASIC, TAG, "App Version not available.");
        }

        return versionCode;
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
    public void addEventToQueue(List<String> tags, String url, String latitude,
                                          String longitude, EventTaskQueue eventTaskQueue) {
        //Checking to make sure hid, appId and domain are not null.
        if (authenticatedClient.getHid() == null || authenticatedClient.getAppId() == null ||
                authenticatedClient.getDomain() == null) {
            log.d(Logger.LogLevel.BASIC, TAG, "One of the Authentication parameters is null");
            return;
        }

        //Checking to make sure either tags or url are part of the request
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
    public Callback<UserRegisterAppResponse> mRegisterCallback =
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
    public boolean canGetRecommendations() {
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

    /**
     * Returns a comma delimited String from input list of strings.
     *
     * @param input List<String>
     * @return String
     */
    public static String getCommaDelimitedString(List<String> input) {
        String output = "";

        if (input != null && input.size() > 0) {
            for (String tag : input) {
                output += tag + ",";
            }

            output = output.substring(0, output.length() - 1);
            output = output.replaceAll(" ", "");
        }

        return output;
    }
}