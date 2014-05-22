package com.sailthru.android.sdk;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpStatus;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Vijay Penemetsa on 5/20/14.
 */
class SailthruClient_Abstract {

    Context mContext;
    ASYNC_RegisterTask mHorIdLoader = null;
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

    protected void makeRegistrationRequest(String appId, String apiKey, String uid,
                                         Identification userType) {
        String storedHid = mAuthenticatedClient.getHid();

        if (UTILS_AppRegister.notNullOrEmpty(storedHid) &&
                !userType.equals(SailthruClient_Abstract.Identification.ANONYMOUS)) {
            Log.d("stored Hid", storedHid);
            API_Manager.getInstance(mAuthenticatedClient).registerUser(mContext,
                    appId, apiKey, storedHid, null, mRegisterCallback);
        } else {
            API_Manager.getInstance(mAuthenticatedClient).registerUser(mContext,
                    appId, apiKey, uid, userType, mRegisterCallback);
        }
    }

    Callback<MODEL_UserRegisterAppResponse> mRegisterCallback = new Callback<MODEL_UserRegisterAppResponse>() {
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