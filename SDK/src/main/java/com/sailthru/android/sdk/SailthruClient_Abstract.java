package com.sailthru.android.sdk;

import android.content.Context;
import android.text.TextUtils;

/**
 * Created by Vijay Penemetsa on 5/20/14.
 */
class SailthruClient_Abstract {

    Context mContext;
    Async_RegisterTask mHorIdLoader = null;
    St_AuthenticatedClient mAuthenticatedClient;

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

    protected boolean passedSanityChecks() {

        return true;
    }

    protected boolean notNullOrEmpty(String input) {
        if (input != null ) {
            if (!TextUtils.isEmpty(input)) {
                return true;
            }
        }

        return false;
    }

    protected boolean notNullAndHasValue(String input, String match) {
        if (input != null) {
            if (input.equals(match)) {
                return true;
            }
        }

        return false;
    }
}
