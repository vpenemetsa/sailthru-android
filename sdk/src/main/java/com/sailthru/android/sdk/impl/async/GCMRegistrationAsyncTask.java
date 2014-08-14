package com.sailthru.android.sdk.impl.async;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.sailthru.android.sdk.Sailthru;
import com.sailthru.android.sdk.impl.client.AuthenticatedClient;
import com.sailthru.android.sdk.impl.external.retrofit.Callback;
import com.sailthru.android.sdk.impl.logger.Logger;
import com.sailthru.android.sdk.impl.logger.STLog;
import com.sailthru.android.sdk.impl.response.UserRegisterAppResponse;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by vijaypenemetsa on 8/7/14.
 */
public class GCMRegistrationAsyncTask extends AsyncTask<Void, Void, Boolean> {

    Context context;
    String env;
    String appId;
    String apiKey;
    String uid;
    String platformAppId;
    String projectNumber;
    Sailthru.Identification userType;
    AuthenticatedClient authClient;
    Callback<UserRegisterAppResponse> callback;
    STLog log;
    GoogleCloudMessaging gcm;
    String regId;

    public GCMRegistrationAsyncTask(Context context, String env, String appId, String apiKey,
                                    String uid, Sailthru.Identification userType,
                                    String platformAppId, String projectNumber,
                                    AuthenticatedClient authClient,
                                    Callback<UserRegisterAppResponse> callback, STLog log) {
        this.context = context;
        this.env = env;
        this.appId = appId;
        this.apiKey = apiKey;
        this.uid = uid;
        this.userType = userType;
        this.platformAppId = platformAppId;
        this.projectNumber = projectNumber;
        this.authClient = authClient;
        this.callback = callback;
        this.log = log;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            gcm = GoogleCloudMessaging.getInstance(context);
            regId = gcm.register(projectNumber);
            log.d(Logger.LogLevel.BASIC, "GCM", regId);

            return true;
        } catch (IOException ex) {
            String msg = "Error :" + ex.getMessage();
            log.e(Logger.LogLevel.BASIC, "GCM", msg);
        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if (aBoolean) {
            storeRegistrationId(context, regId);
            UserRegisterAsyncTask userRegisterAsyncTask = new UserRegisterAsyncTask(context, env,
                    appId, apiKey, uid, userType, platformAppId, regId, authClient,
                    callback);
            userRegisterAsyncTask.execute((Void) null);
        }
    }

    /**
     * Stores the registration ID and the app versionCode.
     *
     * @param context application's context.
     * @param regId registration ID
     */
    private void storeRegistrationId(Context context, String regId) {
        int appVersion = getAppVersion(context);
        authClient.saveGcmRegId(regId);
        authClient.saveAppVersion(Integer.toString(appVersion));
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
}