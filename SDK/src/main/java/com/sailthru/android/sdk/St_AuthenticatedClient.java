package com.sailthru.android.sdk;

import android.content.Context;

import com.sailthru.android.sdk.utils.SecurePreferences;

/**
 * Created by Vijay Penemetsa on 5/20/14.
 */
public class ST_AuthenticatedClient {

    private static ST_AuthenticatedClient mAuthenticatedClient;

    static SecurePreferences mPrefs;
    static Context mContext;

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
    private static String mHid;
    private static String mMode;
    private static String mDomain;
    private static String mApiKey;
    private static String mAppId;
    private static String mIdentification;
    private static String mUid;
    private static String mToken;

    private boolean mConnectedToNetwork;
    private static boolean mCachedRegisterAttempt;

    public static ST_AuthenticatedClient getInstance(Context context) {
        if (mAuthenticatedClient == null) {
            mAuthenticatedClient = new ST_AuthenticatedClient(context);
        }

        return mAuthenticatedClient;
    }

    public ST_AuthenticatedClient(Context context) {
        mPrefs = new SecurePreferences(context);
        mContext = context;
        loadData();
    }

    private static synchronized void loadData() {
        if (mPrefs.containsKey(ST_PREFS_CACHED_HID)) {
            mHid = mPrefs.getString(ST_PREFS_CACHED_HID);
        }
        if (mPrefs.containsKey(ST_SECURE_PREFS_REGISTRATION_MODE)) {
            mMode = mPrefs.getString(ST_SECURE_PREFS_REGISTRATION_MODE);
        }
        if (mPrefs.containsKey(ST_SECURE_PREFS_DOMAIN)) {
            mDomain = mPrefs.getString(ST_SECURE_PREFS_DOMAIN);
        }
        if (mPrefs.containsKey(ST_SECURE_PREFS_API_KEY)) {
            mApiKey = mPrefs.getString(ST_SECURE_PREFS_API_KEY);
        }
        if (mPrefs.containsKey(ST_SECURE_PREFS_APP_ID)) {
            mAppId = mPrefs.getString(ST_SECURE_PREFS_APP_ID);
        }
        if (mPrefs.containsKey(ST_SECURE_PREFS_IDENTIFICATION)) {
            mIdentification = mPrefs.getString(ST_SECURE_PREFS_IDENTIFICATION);
        }
        if (mPrefs.containsKey(ST_SECURE_PREFS_UID)) {
            mUid = mPrefs.getString(ST_SECURE_PREFS_UID);
        }
        if (mPrefs.containsKey(ST_SECURE_PREFS_TOKEN)) {
            mToken = mPrefs.getString(ST_SECURE_PREFS_TOKEN);
        }
        if (mPrefs.containsKey(ST_SECURE_PREFS_CACHED_REGISTER_ATTEMPT)) {
            mCachedRegisterAttempt = true;
        }
    }

    public String getHid() {
        return mHid;
    }

    public synchronized void saveHid(String hid) {
        mPrefs.put(ST_PREFS_CACHED_HID, hid);
        mHid = hid;
    }

    public synchronized void deleteHid() {
        if (mPrefs.containsKey(ST_PREFS_CACHED_HID)) {
            mPrefs.removeValue(ST_PREFS_CACHED_HID);
        }
        mHid = null;
    }

    public static String getMode() {
        return mMode;
    }

    public void setMode(String mode) {
        mPrefs.put(ST_SECURE_PREFS_REGISTRATION_MODE, mode);
        mMode = mode;
    }

    public String getDomain() {
        return mDomain;
    }

    public void setDomain(String domain) {
        mPrefs.put(ST_SECURE_PREFS_DOMAIN, domain);
        mDomain = domain;
    }

    public String getApiKey() {
        return mApiKey;
    }

    public void setApiKey(String apiKey) {
        mPrefs.put(ST_SECURE_PREFS_API_KEY, apiKey);
        mApiKey = apiKey;
    }

    public String getAppId() {
        return mAppId;
    }

    public void setAppId(String appId) {
        mPrefs.put(ST_SECURE_PREFS_APP_ID, appId);
        mAppId = appId;
    }

    public String getIdentification() {
        return mIdentification;
    }

    public void setIdentification(String identification) {
        mPrefs.put(ST_SECURE_PREFS_IDENTIFICATION, identification);
        mIdentification = identification;
    }

    public  String getUid() {
        return mUid;
    }

    public void setUid(String uid) {
        mPrefs.put(ST_SECURE_PREFS_UID, uid);
        mUid = uid;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(String token) {
        mPrefs.put(ST_SECURE_PREFS_TOKEN, token);
        mToken = token;
    }

    public boolean isConnectedToNetwork() {
        return mConnectedToNetwork;
    }

    public void setConnectedToNetwork(boolean connectedToNetwork) {
        mConnectedToNetwork = connectedToNetwork;
    }

    public boolean isCachedRegisterAttempt() {
        return mCachedRegisterAttempt;
    }

    public void setCachedRegisterAttempt() {
        mCachedRegisterAttempt = true;
        mPrefs.put(ST_SECURE_PREFS_CACHED_REGISTER_ATTEMPT, "true");
    }

    public void deleteCachedRegisterAttempt() {
        if (mPrefs.containsKey(ST_SECURE_PREFS_CACHED_REGISTER_ATTEMPT)) {
            mPrefs.removeValue(ST_SECURE_PREFS_CACHED_REGISTER_ATTEMPT);
            mCachedRegisterAttempt = false;
        }
    }
}