package com.sailthru.android.sdk.impl.client;

import android.content.Context;

import com.sailthru.android.sdk.impl.utils.SecurePreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Vijay Penemetsa on 5/20/14.
 *
 * Stores and manages client data using Secure Preferences
 */
public class AuthenticatedClient {

    private static AuthenticatedClient authenticatedClient;

    @Inject
    static SecurePreferences prefs;
    static Context context;

    //Shared prefs keys
    private static final String ST_PREFS_CACHED_HID = "ST_PREFS_CACHED_HID";
    private static final String ST_SECURE_PREFS_REGISTRATION_MODE = "ST_SECURE_PREFS_REGISTRATION_MODE";
    private static final String ST_SECURE_PREFS_DOMAIN = "ST_SECURE_PREFS_DOMAIN";
    private static final String ST_SECURE_PREFS_API_KEY = "ST_SECURE_PREFS_API_KEY";
    private static final String ST_SECURE_PREFS_APP_ID = "ST_SECURE_PREFS_APP_ID";
    private static final String ST_SECURE_PREFS_IDENTIFICATION = "ST_SECURE_PREFS_IDENTIFICATION";
    private static final String ST_SECURE_PREFS_UID = "ST_SECURE_PREFS_UID";
    private static final String ST_SECURE_PREFS_TOKEN = "ST_SECURE_PREFS_TOKEN";
    private static final String ST_SECURE_PREFS_CACHED_REGISTER_ATTEMPT = "ST_SECURE_PREFS_CACHED_REGISTER_ATTEMPT";

    //Stored variables
    private static String hid;
    private static String mode;
    private static String domain;
    private static String apiKey;
    private static String appId;
    private static String identification;
    private static String uid;
    private static String token;

    private boolean connectedToNetwork;
    private static boolean cachedRegisterAttempt;


    public static AuthenticatedClient getInstance(Context context) {
        if (authenticatedClient == null) {
            authenticatedClient = new AuthenticatedClient(context);
        }

        return authenticatedClient;
    }

    public AuthenticatedClient(Context context) {
        prefs = new SecurePreferences(context);
        this.context = context;
        loadData();
    }

    //Loads cached data into memory from Secure Prefs
    private static synchronized void loadData() {
        if (prefs.containsKey(ST_PREFS_CACHED_HID)) {
            hid = prefs.getString(ST_PREFS_CACHED_HID);
        }
        if (prefs.containsKey(ST_SECURE_PREFS_REGISTRATION_MODE)) {
            mode = prefs.getString(ST_SECURE_PREFS_REGISTRATION_MODE);
        }
        if (prefs.containsKey(ST_SECURE_PREFS_DOMAIN)) {
            domain = prefs.getString(ST_SECURE_PREFS_DOMAIN);
        }
        if (prefs.containsKey(ST_SECURE_PREFS_API_KEY)) {
            apiKey = prefs.getString(ST_SECURE_PREFS_API_KEY);
        }
        if (prefs.containsKey(ST_SECURE_PREFS_APP_ID)) {
            appId = prefs.getString(ST_SECURE_PREFS_APP_ID);
        }
        if (prefs.containsKey(ST_SECURE_PREFS_IDENTIFICATION)) {
            identification = prefs.getString(ST_SECURE_PREFS_IDENTIFICATION);
        }
        if (prefs.containsKey(ST_SECURE_PREFS_UID)) {
            uid = prefs.getString(ST_SECURE_PREFS_UID);
        }
        if (prefs.containsKey(ST_SECURE_PREFS_TOKEN)) {
            token = prefs.getString(ST_SECURE_PREFS_TOKEN);
        }
        if (prefs.containsKey(ST_SECURE_PREFS_CACHED_REGISTER_ATTEMPT)) {
            cachedRegisterAttempt = true;
        }
    }

    public String getHid() {
        return hid;
    }

    public synchronized void saveHid(String hid) {
        prefs.put(ST_PREFS_CACHED_HID, hid);
        this.hid = hid;
    }

    public synchronized void deleteHid() {
        if (prefs.containsKey(ST_PREFS_CACHED_HID)) {
            prefs.removeValue(ST_PREFS_CACHED_HID);
        }
        hid = null;
    }

    public static String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        prefs.put(ST_SECURE_PREFS_REGISTRATION_MODE, mode);
        this.mode = mode;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        prefs.put(ST_SECURE_PREFS_DOMAIN, domain);
        this.domain = domain;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        prefs.put(ST_SECURE_PREFS_API_KEY, apiKey);
        this.apiKey = apiKey;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        prefs.put(ST_SECURE_PREFS_APP_ID, appId);
        this.appId = appId;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        prefs.put(ST_SECURE_PREFS_IDENTIFICATION, identification);
        this.identification = identification;
    }

    public  String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        prefs.put(ST_SECURE_PREFS_UID, uid);
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        prefs.put(ST_SECURE_PREFS_TOKEN, token);
        this.token = token;
    }

    public boolean isConnectedToNetwork() {
        return connectedToNetwork;
    }

    public void setConnectedToNetwork(boolean connectedToNetwork) {
        this.connectedToNetwork = connectedToNetwork;
    }

    public boolean isCachedRegisterAttempt() {
        return cachedRegisterAttempt;
    }

    public void setCachedRegisterAttempt() {
        cachedRegisterAttempt = true;
        prefs.put(ST_SECURE_PREFS_CACHED_REGISTER_ATTEMPT, "true");
    }

    public void deleteCachedRegisterAttempt() {
        if (prefs.containsKey(ST_SECURE_PREFS_CACHED_REGISTER_ATTEMPT)) {
            prefs.removeValue(ST_SECURE_PREFS_CACHED_REGISTER_ATTEMPT);
            cachedRegisterAttempt = false;
        }
    }
}