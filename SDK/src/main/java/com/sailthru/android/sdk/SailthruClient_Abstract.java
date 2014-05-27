package com.sailthru.android.sdk;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.sailthru.android.sdk.async.ASYNC_RegisterTask;
import com.sailthru.android.sdk.model.MODEL_UserRegisterAppResponse;
import com.sailthru.android.sdk.utils.UTILS_AppRegister;

import org.apache.http.HttpStatus;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Vijay Penemetsa on 5/20/14.
 */
public class SailthruClient_Abstract {

    Context mContext;
    ASYNC_RegisterTask mAppRegisterTask = null;
    ST_AuthenticatedClient mAuthenticatedClient;

    /**
     * Defines User type for registration
     */
    public enum Identification {
        EMAIL("email"), ANONYMOUS("anonymous");

        private final String name;

        private Identification(String name) {
            this.name = name;
        }

        public String toString() {
            return name;
        }
    }

    /**
     * Defines registration environment
     */
    public enum RegistrationMode {
        DEV("dev"), PROD("prod");

        private final String name;

        private RegistrationMode(String name) {
            this.name = name;
        }

        public String toString() {
            return name;
        }
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
    protected void saveCredentials(String mode, String domain, String apiKey, String appId,
                                   String identification, String uid, String token) {
        mAuthenticatedClient.setMode(mode);
        mAuthenticatedClient.setDomain(domain);
        mAuthenticatedClient.setApiKey(apiKey);
        mAuthenticatedClient.setAppId(appId);
        mAuthenticatedClient.setIdentification(identification);
        mAuthenticatedClient.setUid(uid);
        mAuthenticatedClient.setToken(token);
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
    protected boolean passedSanityChecks(RegistrationMode mode, String domain, String apiKey,
                                         String appId, Identification identification, String uid,
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

        if (identification == Identification.EMAIL && uid == null) {
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
    protected void makeRegistrationRequest(String appId, String apiKey, String uid,
                                         Identification userType) {
        String storedHid = mAuthenticatedClient.getHid();

        if (mAppRegisterTask != null) {
            mAppRegisterTask.cancel(true);
        }

//        API_Manager apiManager = new API_Manager(mAuthenticatedClient);
        if (UTILS_AppRegister.notNullOrEmpty(storedHid) &&
                !userType.equals(SailthruClient_Abstract.Identification.ANONYMOUS)) {
            Log.d("stored Hid", storedHid);
            mAppRegisterTask = new ASYNC_RegisterTask(mContext, appId, apiKey, storedHid, userType,
                    mAuthenticatedClient, mRegisterCallback);
//            apiManager.registerUser(mContext, appId, apiKey, storedHid, null, mRegisterCallback);
        } else {
            Log.d("stored Hid", "****************");
            mAppRegisterTask = new ASYNC_RegisterTask(mContext, appId, apiKey, uid, userType,
                    mAuthenticatedClient, mRegisterCallback);
//            apiManager.registerUser(mContext, appId, apiKey, uid, userType, mRegisterCallback);
        }
        mAppRegisterTask.execute((Void) null);
    }

    /**
     * Callback for Registration request
     */
    protected Callback<MODEL_UserRegisterAppResponse> mRegisterCallback = new Callback<MODEL_UserRegisterAppResponse>() {
        @Override
        public void success(MODEL_UserRegisterAppResponse registerAppResponse, Response response) {
            if (response.getStatus() == HttpStatus.SC_OK) {
                mAuthenticatedClient.saveHid(registerAppResponse.getHid());
                Toast.makeText(mContext, registerAppResponse.getHid(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void failure(RetrofitError error) {
            error.printStackTrace();
        }
    };
}