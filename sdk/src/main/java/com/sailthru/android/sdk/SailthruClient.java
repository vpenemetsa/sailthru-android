package com.sailthru.android.sdk;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.sailthru.android.sdk.impl.client.AuthenticatedClient;
import com.sailthru.android.sdk.impl.async.RegisterAsyncTask;
import com.sailthru.android.sdk.impl.response.UserRegisterAppResponse;
import com.sailthru.android.sdk.impl.utils.AppRegisterUtils;

import org.apache.http.HttpStatus;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Vijay Penemetsa on 5/20/14.
 */
class SailthruClient {

    static Context context;
    static RegisterAsyncTask appRegisterAsyncTask = null;
    static AuthenticatedClient authenticatedClient;

    @Inject
    static AppRegisterUtils appRegisterUtils;

    public SailthruClient(Context context, AuthenticatedClient authenticatedClient) {
        this.authenticatedClient = authenticatedClient;
        this.context = context;
    }

    /**
     * Saves credentials from client to Shared Preferences
     *
     * @param mode
     * @param domain
     * @param apiKey
     * @param appId
     * @param identification
     * @param uid
     * @param token
     */
    protected static void saveCredentials(String mode, String domain, String apiKey, String appId,
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
     * @param mode
     * @param domain
     * @param apiKey
     * @param appId
     * @param identification
     * @param uid
     * @param token
     * @return
     */
    protected static boolean passedSanityChecks(Sailthru.RegistrationMode mode, String domain, String apiKey,
                                                String appId, Sailthru.Identification identification, String uid,
                                                String token) {

        boolean passedChecks = true;

        if (mode == null) {
            passedChecks = false;
            Log.e("SailthruSDK", "Mode cannot be set to null");
        }
        if (domain == null) {
            passedChecks = false;
            Log.e("SailthruSDK", "Domain cannot be null");
        }
        if (apiKey == null) {
            passedChecks = false;
            Log.e("SailthruSDK", "API Key cannot be null");
        }
        if (appId == null) {
            passedChecks = false;
            Log.e("SailthruSDK", "APP ID cannot be null");
        }
        if (identification == null) {
            Log.e("SailthruSDK", "Identification cannot be null");
            passedChecks = false;
        }

        if (identification == Sailthru.Identification.EMAIL && uid == null) {
            passedChecks = false;
            Log.e("SailthruSDK", "UID cannot be null when Identification is set to EMAIL");
        }

        return passedChecks;
    }

    /**
     * Makes a call to the ApiManager with input credentials based on UserType
     *
     * @param appId
     * @param apiKey
     * @param uid
     * @param userType
     */
    protected static void makeRegistrationRequest(String appId, String apiKey, String uid,
                                                  Sailthru.Identification userType) {

        // Cancel any running AppRegister calls
        if (appRegisterAsyncTask != null) {
            appRegisterAsyncTask.cancel(true);
        }

        appRegisterAsyncTask = new RegisterAsyncTask(context, appId, apiKey, uid, userType,
                authenticatedClient, mRegisterCallback);
        appRegisterAsyncTask.execute((Void) null);
    }

    /**
     * Callback for Registration request
     */
    protected static Callback<UserRegisterAppResponse> mRegisterCallback = new Callback<UserRegisterAppResponse>() {
        @Override
        public void success(UserRegisterAppResponse registerAppResponse, Response response) {
            if (response.getStatus() == HttpStatus.SC_OK) {
                authenticatedClient.saveHid(registerAppResponse.getHid());
                Toast.makeText(context, registerAppResponse.getHid(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void failure(RetrofitError error) {
            error.printStackTrace();
        }
    };
}